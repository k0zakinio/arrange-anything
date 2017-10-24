#!/usr/bin/env bash

set -e

function stop_postgres_if_running () {
    RUNNING_POSTGRES_CONTAINER=$(docker ps | grep postgres | awk '{ print $1 }')
    if [ ${RUNNING_POSTGRES_CONTAINER} ]
        then
            docker stop ${RUNNING_POSTGRES_CONTAINER}
    fi
}

stop_postgres_if_running
docker-compose down
./remove_arrange_anything.sh
./start-postgres.sh
./gradlew clean build fatJar --stacktrace
docker build -t k0zakinio/arrange-anything . 
stop_postgres_if_running
