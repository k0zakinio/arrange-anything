#!/bin/bash

./stop.sh
docker run -d -p 8080:8080 aa2
