#!/bin/bash

# Change to the directory of the script
cd "$(dirname "$0")" || exit

NODE1_LOG="../node1.log"
NODE2_LOG="../node2.log"
NODE3_LOG="../node3.log"

log_files=("$NODE1_LOG" "$NODE2_LOG" "$NODE3_LOG")

for file in "${log_files[@]}"; do
    if [ -e "$file" ]; then
        rm "$file"
        echo "removed: $file"
    fi
done

nohup kafka-server-start "../config/node.1.properties" > $NODE1_LOG 2>&1 &
nohup kafka-server-start "../config/node.2.properties" > $NODE2_LOG 2>&1 &
nohup kafka-server-start "../config/node.3.properties" > $NODE3_LOG 2>&1 &

echo "Kafka nodes are starting in the background. Logs are being written to node1.log, node2.log, and node3.log."