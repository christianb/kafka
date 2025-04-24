# Kafka 
This is all about learning Kafka using practical examples.

## Setup
Installed via Homebrew.
```bash
brew install kafka
brew info kafka
==> kafka: stable 4.0.0 (bottled)
```

### Start Server:
```bash
KAFKA_CLUSTER_ID=$(kafka-storage random-uuid)
kafka-storage format -t $KAFKA_CLUSTER_ID -c kraft.properties
kafka-server-start kraft.properties
```

- when starting the server for the very first time: `./scripts/generate-cluster-id.sh`
- then `./scripts/start-kafka-server.sh`

### Create Topic
```bash
kafka-topics --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3
```

or just run `./scripts/create-topic.sh <NAME> <NUM_PARTITIONS`

### List Topics
```bash
kafka-topics --bootstrap-server localhost:9092 --describe 
```

or just run `./scripts/list-topics.sh`

### Create Producer
```bash
kafka-console-producer --bootstrap-server localhost:9092 --topic first_topic --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --producer-property acks-all
```

## Consumers

### Create Consumer
```bash
kafka-console-consumer  --bootstrap-server localhost:9092 --topic first_topic
```

### Create Consumer with Group
```bash
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic third_topic --group my-first-application
```

### List Consumer Groups
```bash
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

### Describe one specific group
```bash
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my-second-application
```

### Display 
```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic second_topic --from-beginning --formatter org.apache.kafka.tools.consumer.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true
```