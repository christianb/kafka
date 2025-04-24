package demo

import config.AutoOffsetReset
import config.KafkaFactory
import config.PartitionAssignmentStrategy
import config.log
import config.use
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.common.TopicPartition
import java.time.Duration

fun main() {
    KafkaFactory.consumer<String, String>(
        topic = "kotlin_demo",
        groupId = "my-kotlin-application",
        autoOffsetReset = AutoOffsetReset.EARLIEST,
        partitionAssignmentStrategy = PartitionAssignmentStrategy.CooperativeSticky,
        rebalanceListener = rebalanceListener
    ).use {
        while (true) {
            log.info("polling ...")
            val consumerRecords = poll(Duration.ofMillis(1000))
            for (record in consumerRecords) {
                log.info("Key: ${record.key()}, Value: ${record.value()}")
                log.info("Partition: ${(record)}, Offset: ${record.offset()}")
            }
        }
    }
}

private val rebalanceListener = object : ConsumerRebalanceListener {
    override fun onPartitionsRevoked(partitions: Collection<TopicPartition?>?) {
        log.info("onPartitionsRevoked callback triggered");
        // log.info("Committing offsets: " + currentOffsets);
    }

    override fun onPartitionsAssigned(partitions: Collection<TopicPartition?>?) {
        log.info("onPartitionsAssigned callback triggered");
    }

}