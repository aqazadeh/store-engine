#!/bin/bash

set -e

echo "Starting docker services"

docker compose \
  -f docker-compose-base.yaml \
  -f docker-compose-postgres.yaml \
  up -d

sleep 3

docker ps