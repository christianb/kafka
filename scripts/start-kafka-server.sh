#!/bin/bash

# Change to the directory of the script
cd "$(dirname "$0")" || exit

# need clusterId !
kafka-server-start ../kraft.properties
