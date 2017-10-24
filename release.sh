#!/usr/bin/env bash

docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker-compose pull web
docker-compose up --force-recreate -d
