#!/bin/bash

# Change to the directory of the script
cd "$(dirname "$0")" || exit

NODE1_PROPERTIES="../node.1.properties"
NODE1_LOG="../node1.log"

NODE2_PROPERTIES="../node.2.properties"
NODE2_LOG="../node2.log"

NODE3_PROPERTIES="../node.3.properties"
NODE3_LOG="../node3.log"

# Start Node 1
nohup kafka-server-start $NODE1_PROPERTIES > $NODE1_LOG 2>&1 &

# Start Node 2
nohup kafka-server-start $NODE2_PROPERTIES > $NODE2_LOG 2>&1 &

# Start Node 3
nohup kafka-server-start $NODE3_PROPERTIES > $NODE3_LOG 2>&1 &

# Optional: Print a message indicating that the nodes have been started
echo "Kafka nodes are starting in the background. Logs are being written to node1.log, node2.log, and node3.log."