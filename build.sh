#!/usr/bin/env bash

set -e

function stop_postgres_if_running () {
    RUNNING_POSTGRES_CONTAINER=$(docker ps | grep postgres | awk '{ print $1 }')
    if [ ${RUNNING_POSTGRES_CONTAINER} ]
        then
            docker stop ${RUNNING_POSTGRES_CONTAINER}
    fi
}

function remove_arrange-anything_container () {
    ARRANGE_ANYTHING_CONTAINER=$(docker ps | grep 'arrange-anything' | awk '{ print $1 }')
    if [ ${ARRANGE_ANYTHING_CONTAINER} ]
    then
        docker rm ${ARRANGE_ANYTHING_CONTAINER}
        docker rmi k0zakinio/arrange-anything
    fi
}

stop_postgres_if_running
docker-compose down
remove_arrange-anything_container
./start-postgres.sh
./gradlew clean build fatJar
docker build -t k0zakinio/arrange-anything .
stop_postgres_if_running