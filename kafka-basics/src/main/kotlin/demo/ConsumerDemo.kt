package demo

import kafka.course.demo.AutoOffsetReset
import kafka.course.demo.KafkaFactory
import kafka.course.demo.log
import kafka.course.demo.use
import java.time.Duration

fun main() {
    KafkaFactory.consumer<String, String>(
        topic = "demo_java",
        groupId = "my-java-application",
        autoOffsetReset = AutoOffsetReset.EARLIEST
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


