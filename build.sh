#!/bin/bash

./gradlew clean build fatJar
docker build -t aa2 .
