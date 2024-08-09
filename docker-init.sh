#!/bin/bash

#Задаем конфигурационный файл для kubectl
export KUBECONFIG=/opt/config/config

cd /opt/my-chart

echo "helm dependency============================================="
helm dependency update chart
echo "helm upgrade================================================"
helm upgrade app chart

