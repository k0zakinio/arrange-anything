#!/usr/bin/env bash



docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker-compose down
./remove_arrange_anything.sh
docker-compose pull web
docker-compose up --force-recreate -d

