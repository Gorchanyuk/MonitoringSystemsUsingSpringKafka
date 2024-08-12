#!/bin/bash

HELM_NAME=app
CHART_FOLDER="/opt/my-chart/chart"
MY_VALUES="/opt/my-chart/my.yaml"

cd /opt/my-chart || exit

#Задаем конфигурацию для kubectl
kubectl config set-cluster minikube --server=https://minikube:8443 --certificate-authority=/opt/config/ca.crt
kubectl config set-credentials minikube --client-key=/opt/config/client.key --client-certificate=/opt/config/client.crt
kubectl config set-context minikube --cluster=minikube --user=minikube
kubectl config use-context minikube


HELM_EXISTS=$(helm ls | grep -c ${HELM_NAME})
if (( "${HELM_EXISTS}" == 0 )); then
  echo "### HELM: install  ====================================================="
  helm install "${HELM_NAME}" "${CHART_FOLDER}" -f "${MY_VALUES}"
else
  echo "### HELM: upgrade  ====================================================="
  helm upgrade "${HELM_NAME}" "${CHART_FOLDER}" -f "${MY_VALUES}"
fi
