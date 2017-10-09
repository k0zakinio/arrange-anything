#!/usr/bin/env bash

docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
./build.sh
docker-compose up --force-recreate -d