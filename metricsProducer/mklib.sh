#!/bin/bash

CDIR=$(pwd)
declare -a my_array=("/src" "/pom.xml" "/Dockerfile")
imageName=producer
repoName=bezbasheniy

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
  total_hash=""

  for element in "${@:2}"; do
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

function add_tag() {
  tag_name=$1
  git tag -a "${tag_name}" -m "Соответствует образу сборки с тегом - ${tag_name}"
}

function build_all() {
  is_master && is_committed

  hash=$(get_hash "${CDIR}" "${my_array[@]}")
  check_tag "${repoName}/${imageName}" "${hash}"

  mvn dockerfile:build -Dproject.image.name="${imageName}" -Dtag.version="${hash}"
  status=$?

  if [ "${status}" -eq 1 ]; then
    hash="Error_${hash}"
  fi

  add_tag "${hash}"

}

build_all