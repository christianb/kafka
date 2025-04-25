package demo

import config.AutoOffsetReset
import config.KafkaFactory
import config.log
import config.use
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import java.time.Duration
import java.util.Properties

fun main() {
    val properties = Properties().apply {
        setProperty(GROUP_ID_CONFIG, "my-java-application")
        setProperty(AUTO_OFFSET_RESET_CONFIG, AutoOffsetReset.EARLIEST.value)
    }
    KafkaFactory.consumer<String, String>(topic = "demo_java", properties).use {
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