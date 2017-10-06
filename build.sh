#!/usr/bin/env bash

RUNNING_DOCKER_CONTAINERS=$(docker ps | awk 'NR>1 { print $1 }')
docker kill ${RUNNING_DOCKER_CONTAINERS}
./gradlew clean build fatJar
docker rm arrange-anything
docker rmi k0zakinio/arrange-anything
docker build --no-cache -t arrange-anything .
docker tag arrange-anything k0zakinio/arrange-anything