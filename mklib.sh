#!/bin/bash

CDIR=$(pwd)
REPO_NAME=bezbasheniy
MY_VALUES="./my.yaml"

#Проверка, что находимся в мастере и что совпадаем с удаленным репо
is_master() {
  # Получаем имя текущей ветки
  local CURRENT_BRANCH
  local LOCAL_COMMIT
  local REMOTE_COMMIT
  CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

  # Проверяем, является ли текущая ветка мастер-веткой или основной веткой
  if [[ "${CURRENT_BRANCH}" != "master" ]] && [[ "${CURRENT_BRANCH}" != "main" ]]; then
    echo "Для сборки образа нужно находиться в ветке master или main"
    exit 1
  fi

  # Проверяем, что текущий коммит совпадает с удаленным
  git fetch origin "${CURRENT_BRANCH}"
  LOCAL_COMMIT=$(git rev-parse HEAD)
  REMOTE_COMMIT=$(git rev-parse origin/"${CURRENT_BRANCH}")

  if [[ "${LOCAL_COMMIT}" != "${REMOTE_COMMIT}" ]]; then
    echo "Локальная ветка не на последнем коммите"
    exit 1
  fi

  echo "Находимся в ветке ${CURRENT_BRANCH} и на последнем коммите"
}

#Проверка, что отсутствуют незафиксированные изменения
is_committed() {
  status_output=$(git status --porcelain | grep -cv '^??')
  if [[ "${status_output}" != 0 ]]; then
    echo "Есть незафиксированные изменения в проекте"
    exit 1
  fi
}

#Вычисляем хеш по файлам и пакетам
get_hash() {
  local MY_ARRAY=("src" "pom.xml" "Dockerfile")
  local TOTAL_HASH=""

  for element in "${MY_ARRAY[@]}"; do
    local item="$1/${element}"
    if [[ ! -e "${item}" ]]; then
      continue
    fi
    local HASH=""
    if [[ -d "${item}" ]]; then
      HASH="$(find "${item}" -type f -exec sha256sum {} + | awk '{ print $1 }')"
    else
      HASH="$(sha256sum "${item}" | awk '{ print $1 }')"
    fi
    TOTAL_HASH+="${HASH}"
  done

  echo -n "${TOTAL_HASH}" | sha256sum | head -c 8
}

# Проверяем наличие сборки в локальном репо
is_exist_local() {
  local IMAGE_NAME=$1
  local TAG_NAME=$2
  local RESULT

  RESULT=$(docker images -q "${IMAGE_NAME}":"${TAG_NAME}")
  if [[ -z "${RESULT}" ]]; then
    return 1
  fi
}

# Проверяем наличие сборки в удаленном репо
is_exist_remote() {
  local REPOSITORY=$1
  local TAG=$2
  local URL="https://hub.docker.com/v2/repositories/${REPOSITORY}/tags/${TAG}"
  local HTTP_CODE

  HTTP_CODE=$(curl -s -o /dev/null -w "%{HTTP_CODE}" "${URL}")
  if [[ "${HTTP_CODE}" != 200 ]]; then
    return 1
  fi
}

# Проверяем наличие такой сборки
check_tag() {
  local REPO=$1
  local TAG=$2
  if is_exist_local "$REPO" "$TAG"; then
    echo "Сборка найдена в локальном репо"
    exit 1
  fi
  if is_exist_remote "${REPO}" "${TAG}"; then
    echo "Сборка найдена в удаленном репо"
    exit 1
  fi
  echo "Сборка не найдена"
}

# Добавляет тег к комиту
add_tag() {
  local TAG_NAME=$1
  git tag -a "${TAG_NAME}" -m "Соответствует образу сборки с тегом - ${TAG_NAME}"
  if [[ $? -eq 1 ]]; then
    echo "Тег ${TAG_NAME} уже существует"
  fi
}

# Финальная функция, запускает сборку
build_all() {
  local HASH
  is_master && is_committed

  HASH=$(get_hash "${CDIR}")
  check_tag "${REPO_NAME}/${IMAGE_NAME}" "${HASH}"

  mvn docker:build docker:push -Dproject.image.name="${REPO_NAME}/${IMAGE_NAME}" -Dtag.version="${HASH}"
  status=$?

  if [[ "${status}" -eq 1 ]]; then
    HASH="Error_${HASH}"
    echo "Произошла ошибка во время сборки образа"
  fi
  add_tag "${HASH}"

  cd ../
  if [[ ! -f "${MY_VALUES}" ]]; then
    touch "${MY_VALUES}"
  fi
  IMAGE_VERSION="${REPO_NAME}/${IMAGE_NAME}:${HASH}" yq e -i ".${CHART_NAME}.image = strenv(IMAGE_VERSION)" "${MY_VALUES}"

  docker build -t helm .
}

build_all
