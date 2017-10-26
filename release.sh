#!/usr/bin/env bash

set -e

docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker-compose down
docker-compose pull web
docker-compose up --force-recreate -d