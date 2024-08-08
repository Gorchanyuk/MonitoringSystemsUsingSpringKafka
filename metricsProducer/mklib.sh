#!/bin/bash

CDIR=$(pwd)
chartName=producer
imageName=producer
repoName=bezbasheniy
HELM_NAME=app
CHART_FOLDER="./../chart"
MY_VALUES="./../my.yaml"

#Проверка, что находимся в мастере и что совпадаем с удаленным репо
function is_master() {
  # Получаем имя текущей ветки
  current_branch=$(git rev-parse --abbrev-ref HEAD)

  # Проверяем, является ли текущая ветка мастер-веткой или основной веткой
  if [ "$current_branch" != "master" ] && [ "$current_branch" != "main" ]; then
    echo "Для сборки образа нужно находиться в ветке master или main"
    exit 1
  fi

  # Проверяем, что текущий коммит совпадает с удаленным
  git fetch origin "$current_branch"
  local_commit=$(git rev-parse HEAD)
  remote_commit=$(git rev-parse origin/"$current_branch")

  if [ "$local_commit" != "$remote_commit" ]; then
    echo "Локальная ветка не на последнем коммите"
    exit 1
  fi

  echo "Находимся в ветке $current_branch и на последнем коммите"
}

#Проверка, что отсутствуют незафиксированные изменения
function is_committed() {
  status_output=$(git status --porcelain | grep -cv '^??')
  if [ "${status_output}" != 0 ]; then
    echo "Есть незафиксированные изменения в проекте"
    exit 1
  fi
}

#Вычисляем хеш по файлам и пакетам
function get_hash() {
  declare -a my_array=("/src" "/pom.xml" "/Dockerfile")
  total_hash=""

  for element in "${my_array[@]}"; do
    item="$1/$element"
    if [ ! -e "$item" ]; then
      continue
    fi
    hash=""
    if [ -d "$item" ]; then
      hash="$(find "$item" -type f -exec sha256sum {} + | awk '{ print $1 }')"
    else
      hash="$(sha256sum "$item" | awk '{ print $1 }')"
    fi
    total_hash+="${hash}"
  done

  echo -n "$total_hash" | sha256sum | head -c 8
}

# Проверяем наличие сборки в локальном репо
function is_exist_local() {
  local image_name=$1
  local tag_name=$2

  result=$(docker images -q "$image_name":"$tag_name")
  if [ -z "$result" ]; then
    return 1
  fi
}

# Проверяем наличие сборки в удаленном репо
function is_exist_remote() {
  local repository=$1
  local tag=$2
  local url="https://hub.docker.com/v2/repositories/${repository}/tags/${tag}"
  local http_code

  http_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
  if [ "$http_code" != 200 ]; then
    return 1
  fi
}

# Проверяем наличие такой сборки
function check_tag() {
  local repo=$1
  local tag=$2
  if is_exist_local "$repo" "$tag"; then
    echo "Сборка найдена в локальном репо"
    exit 1
  fi
  if is_exist_remote "$repo" "$tag"; then
    echo "Сборка найдена в удаленном репо"
    exit 1
  fi
  echo "Сборка не найдена"
}
# Добавляет тег к комиту
function add_tag() {
  tag_name=$1
  git tag -a "${tag_name}" -m "Соответствует образу сборки с тегом - ${tag_name}"
  if [ $? -eq 1 ]; then
    echo "Тег ${tag_name} уже существует"
  fi
}

# Запуск helm чарта
function helm_run() {
  STANDID=$1
  echo "### HELM: update dependency  ============================================"
#  TODO
  #  helm dependency update $STANDID

  HELM_EXISTS=$(helm ls | grep -c ${HELM_NAME})
  if [ "${HELM_EXISTS}" == 0 ]; then
    echo "### HELM: install  ====================================================="
    helm install ${HELM_NAME} "$STANDID" -f "$MY_VALUES"
  else
    echo "### HELM: upgrade  ====================================================="
    helm upgrade ${HELM_NAME} $STANDID -f "$MY_VALUES"
  fi
}

# Финальная функция, запускает сборку
function build_all() {
  is_master && is_committed

  hash=$(get_hash "${CDIR}")
  check_tag "${repoName}/${imageName}" "${hash}"

  mvn docker:build -Dproject.image.name="${repoName}/${imageName}" -Dtag.version="${hash}"
  status=$?

  if [ "${status}" -eq 1 ]; then
    hash="Error_${hash}"
    echo "Произошла ошибка во время сборки образа"
  fi

  add_tag "${hash}"

  if [ ! -f "$MY_VALUES" ]; then
    touch "$MY_VALUES"
  fi
  IMAGE_VERSION="${repoName}/${imageName}:${hash}" yq e -i ".${chartName}.image = strenv(IMAGE_VERSION)" "$MY_VALUES"
  helm_run "${CHART_FOLDER}"
}

build_all
