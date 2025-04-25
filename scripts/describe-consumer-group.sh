#!/bin/bash

GROUP_ID=$1

kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group "${GROUP_ID}"