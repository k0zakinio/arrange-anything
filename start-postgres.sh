#!/usr/bin/env bash

docker run -d -e POSTGRES_USER=testuser -e POSTGRES_PASSWORD=testpassword -e POSTGRES_DB=testdb -v /var/lib/postgres/data:/var/lib/postgresql/data postgres:9.6