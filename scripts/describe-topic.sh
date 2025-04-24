#!/bin/bash

TOPIC_NAME=$1
kafka-topics --describe --topic "$TOPIC_NAME" --bootstrap-server localhost:9092
