#!/bin/bash
set -e

# This script runs during container initialization.
# It creates the application databases and then runs the per-db SQL files.

echo "Creating databases..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
CREATE DATABASE userdb;
CREATE DATABASE exercisedb;
CREATE DATABASE workoutdb;
EOSQL

echo "Running SQL files for each database..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=userdb -f /docker-entrypoint-initdb.d/userdb.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=exercisedb -f /docker-entrypoint-initdb.d/exercisedb.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=workoutdb -f /docker-entrypoint-initdb.d/workoutdb.sql

echo "Database initialization complete."
