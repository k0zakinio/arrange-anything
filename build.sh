#!/bin/bash

./gradlew clean build fatJar --stop
docker build -t aa2 .
