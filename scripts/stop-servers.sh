#!/bin/bash

# Change to the directory of the script
cd "$(dirname "$0")" || exit

NODE1_LOG="../node1.log"
NODE2_LOG="../node2.log"
NODE3_LOG="../node3.log"

kafka-server-stop

rm $NODE1_LOG $NODE2_LOG $NODE3_LOG