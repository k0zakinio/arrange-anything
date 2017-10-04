#!/bin/bash

RUNNING_DOCKER_CONTAINERS=$(docker ps | awk 'NR>1 { print $1 }')
docker stop $RUNNING_DOCKER_CONTAINERS
