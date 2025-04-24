#!/bin/bash

topics=$(kafka-topics --list --bootstrap-server localhost:9092)
for topic in $topics; do
    kafka-topics --delete --topic "$topic" --bootstrap-server localhost:9092
done