.EXPORT_ALL_VARIABLES:

package:
	mvn clean package

docker-build:
	docker build --rm -f Dockerfile -t grpc-server:latest .

docker-run:
	docker run --rm grpc-server:latest .

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down -v
