#!/bin/bash

RUNNING_DOCKER_CONTAINERS=$(docker ps | awk 'NR>1 { print $1 }')
docker kill ${RUNNING_DOCKER_CONTAINERS}
