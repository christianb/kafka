#!/bin/bash

TOPIC_NAME=$1
NUM_PARTITIONS=$2

kafka-topics --bootstrap-server localhost:9092 --topic "${TOPIC_NAME}" --create --partitions "${NUM_PARTITIONS}" --replication-factor 2