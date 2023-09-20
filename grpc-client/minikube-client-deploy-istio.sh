#!/usr/bin/env zsh
#to use minikube docker daemon for building and fetching image
eval $(minikube -p minikube docker-env)

mvn clean package
make docker-build
kubectl config set-context --current --namespace=grpc-istio
kubectl delete -f k8/svc.yaml
kubectl delete -f k8/deployment-istio.yaml
kubectl delete -f k8/deployment.yaml
kubectl delete -f k8/ingress.yaml
kubectl delete -f k8/Gateway_Route.yaml
echo "---------Resources Cleaned----------"
kubectl apply -f k8/namespace.yaml
kubectl label namespace grpc-istio istio-injection=enabled
kubectl apply -f k8/deployment-istio.yaml
kubectl apply -f k8/svc.yaml
kubectl apply -f k8/Gateway_Route.yaml
kubectl run dnsutils --image=tutum/dnsutils --command -- sleep infinity
echo "---------Grpc Client Deployed-------"
kubectl get all