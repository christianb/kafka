package demo

import config.AutoOffsetReset
import config.KafkaFactory
import config.log
import config.use
import java.time.Duration

fun main() {
    KafkaFactory.consumer<String, String>(
        topic = "kotlin_demo",
        groupId = "my-kotlin-application",
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