#!/bin/bash

# Получаем текущую версию
CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

# Разбиваем версию на части
IFS='.' read -r -a VERSION_PARTS <<< "$CURRENT_VERSION"

# Инкрементируем минорную версию
VERSION_PARTS[1]=$((VERSION_PARTS[1] + 1))

# Формируем новую версию
NEW_VERSION="${VERSION_PARTS[0]}.${VERSION_PARTS[1]}.${VERSION_PARTS[2]}"

# Устанавливаем новую версию
mvn versions:set -DnewVersion=$NEW_VERSION