FROM ubuntu:latest

# Установка Helm
RUN apt-get update && \
    apt-get install wget tar iputils-ping -y && \
    wget https://get.helm.sh/helm-v3.8.0-linux-amd64.tar.gz && \
    tar -zxvf helm-v3.8.0-linux-amd64.tar.gz && \
    mv linux-amd64/helm /usr/local/bin/helm && \
    rm -rf helm-v3.8.0-linux-amd64.tar.gz linux-amd64 \

# Установка kubectl
RUN apt-get update && \
    apt-get install -y apt-transport-https ca-certificates curl gnupg && \
    touch /etc/apt/keyrings && \
    curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | \
    gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg && \
    chmod 644 /etc/apt/keyrings/kubernetes-apt-keyring.gpg && \
    echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | \
    tee /etc/apt/sources.list.d/kubernetes.list && \
    chmod 644 /etc/apt/sources.list.d/kubernetes.list && \
    apt-get update && \
    apt-get install -y kubectl

WORKDIR /opt/my-chart
COPY ./chart /opt/my-chart/chart
COPY ./chart-consumer /opt/my-chart/chart-consumer
COPY ./chart-producer /opt/my-chart/chart-producer
COPY ./minikube.config/ /opt/config/
COPY ./docker-init.sh /opt/docker-init.sh
COPY ./my.yaml /opt/my-chart/my.yaml

RUN helm dependency update chart

CMD ["/opt/docker-init.sh"]
