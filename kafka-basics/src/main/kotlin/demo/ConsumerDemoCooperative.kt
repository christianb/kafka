package demo

import kafka.course.demo.AutoOffsetReset
import kafka.course.demo.KafkaFactory
import kafka.course.demo.PartitionAssignmentStrategy
import kafka.course.demo.log
import kafka.course.demo.use
import java.time.Duration

fun main() {
    KafkaFactory.consumer<String, String>(
        topic = "kotlin_demo",
        groupId = "my-kotlin-application",
        autoOffsetReset = AutoOffsetReset.EARLIEST,
        partitionAssignmentStrategy = PartitionAssignmentStrategy.CooperativeSticky,
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


