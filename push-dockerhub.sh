#!/bin/bash

./build.sh
docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker push arrange-anything