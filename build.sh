#!/usr/bin/env bash

set -e

docker-compose down
./remove_arrange_anything.sh
./start-postgres.sh
./gradlew clean build fatJar --stacktrace
docker build -t k0zakinio/arrange-anything . 
