#!/bin/bash

set -e

./build.sh
docker login --username $DOCKERHUB_USERNAME --password $DOCKERHUB_PASSWORD
docker push k0zakinio/arrange-anything