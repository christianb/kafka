#!/bin/bash

# Change to the directory of the script
cd "$(dirname "$0")" || exit

KAFKA_CLUSTER_ID=$(kafka-storage random-uuid)
kafka-storage format -t "$KAFKA_CLUSTER_ID" -c ../kraft.properties