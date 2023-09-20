First Start minikube

```shell
minikube start
```
Grpc Server will be exposing port 9090. 
Start all Grpc Server pods without istio-injection enabled
```shell
bash minkube-server-deploy.sh
```
Grpc Client will expose ports 8080 and 9090
8080: for HTTP calls
9090: for GRPC channel with server
To start all Grpc Client pods without istio-injection enabled
```shell
bash minkube-client-deploy.sh
```

To setup infra with istio-injection=enabled

```shell
istioctl install
```
Then start server 
```shell
bash minikube-server-deploy-istio.sh
```

Then start client
```shell
bash minikube-client-deploy-istio.sh
```