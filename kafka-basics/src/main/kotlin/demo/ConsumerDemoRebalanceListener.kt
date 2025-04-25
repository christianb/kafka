package demo

import config.AutoOffsetReset
import config.KafkaFactory
import config.PartitionAssignmentStrategy
import config.log
import config.use
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.util.Properties

fun main() {
    val properties = Properties().apply {
        setProperty(GROUP_ID_CONFIG, "my-kotlin-application")
        setProperty(AUTO_OFFSET_RESET_CONFIG, AutoOffsetReset.EARLIEST.value)
        setProperty(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, PartitionAssignmentStrategy.CooperativeSticky.value)
    }

    KafkaFactory.consumer<String, String>(topic = "kotlin_demo", properties, rebalanceListener).use {
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