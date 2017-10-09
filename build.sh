#!/usr/bin/env bash

docker-compose down
./gradlew clean build fatJar
docker rm arrange-anything
docker rmi k0zakinio/arrange-anything
docker build -t arrange-anything .
docker tag arrange-anything k0zakinio/arrange-anything