#!/usr/bin/env bash

docker run -d -p 5432:5432 \
    -e POSTGRES_PASSWORD=testpassword \
    -e POSTGRES_DB=testdb \
    -v /var/lib/aa_postgres_data:/var/lib/postgresql/data \
    postgres:9.6