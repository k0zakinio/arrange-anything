#!/usr/bin/env bash

docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker-compose up