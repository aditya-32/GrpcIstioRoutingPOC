.EXPORT_ALL_VARIABLES:

package:
	mvn clean package

docker-build:
	docker build --rm -f Dockerfile -t grpc-client:latest .
docker-run:
	docker run --rm grpc-client:latest .

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down -v
