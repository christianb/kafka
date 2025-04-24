#!/bin/bash

# Default values
REPLICAS=2
TOPIC=""
PARTITIONS=1

# Function to display usage
usage() {
    echo "Usage: $0 --topic <topic_name> --replicas <number> --partitions <number>"
    exit 1
}

# Parse command-line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --replicas)
            REPLICAS="$2"
            shift 2
            ;;
        --topic)
            TOPIC="$2"
            shift 2
            ;;
        --partitions)
            PARTITIONS="$2"
            shift 2
            ;;
        *)
            echo "Unknown parameter passed: $1"
            usage
            ;;
    esac
done

# Check if required arguments are provided
if [[ -z "$TOPIC" ]]; then
    echo "Error: --topic is required and cannot be empty."
    usage
fi

# Now you can use the variables
echo "Number of replicas: $REPLICAS"
echo "Topic: $TOPIC"
echo "Number of partitions: $PARTITIONS"

# kafka-topics --bootstrap-server localhost:9092 --topic "${TOPIC_NAME}" --create --partitions "${NUM_PARTITIONS}" --replication-factor 2