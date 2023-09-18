#!/usr/bin/env zsh
#to use minikube docker daemon for building and fetching image
eval $(minikube -p minikube docker-env)

mvn clean package
make docker-build
kubectl config set-context --current --namespace=grpc-istio
kubectl delete -f k8/svc.yaml
kubectl delete -f k8/deployment.yaml
kubectl delete -f k8/network.yaml
echo "---------Resources Cleaned----------"
kubectl apply -f k8/namespace.yaml
kubectl apply -f k8/deployment.yaml
kubectl apply -f k8/svc.yaml
kubectl apply -f k8/network.yaml
echo "---------Grpc Server Deployed-------"
kubectl get all