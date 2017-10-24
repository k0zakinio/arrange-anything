#!/usr/bin/env bash

ARRANGE_ANYTHING_CONTAINER=$(docker ps -a | grep 'arrange-anything' | awk '{ print $1 }')
ARRANGE_ANYTHING_IMAGES=$(docker images | grep 'arrange-anything' | awk '{ print $3 }')
if [[ ${ARRANGE_ANYTHING_CONTAINER} ]]
then
    docker rm ${ARRANGE_ANYTHING_CONTAINER}
    docker rmi k0zakinio/arrange-anything
    docker rmi arrange-anything
fi;

if [[ ${ARRANGE_ANYTHING_IMAGES} ]]
then
    docker rmi ${ARRANGE_ANYTHING_IMAGES}
fi;