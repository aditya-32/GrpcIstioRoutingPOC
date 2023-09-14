First Start minikube

```shell
minikube start
```
Grpc Server will be exposing port 9090
To start all Grpc Server pods
```shell
bash minkube-server-deploy.sh
```
Grpc Client will expose ports 8080 and 9090
8080: for HTTP calls
9090: for GRPC channel with server
To start all Grpc Client pods
```shell
bash minkube-client-deploy.sh
```