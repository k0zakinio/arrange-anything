#!/bin/bash

./build.sh
docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker tag arrange-anything k0zakinio/arrange-anything
docker push k0zakinio/arrange-anything