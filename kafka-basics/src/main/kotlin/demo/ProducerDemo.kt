package demo

import config.KafkaFactory
import config.use
import org.apache.kafka.clients.producer.ProducerRecord

fun main() {
    val producerRecord = ProducerRecord<String, String>(
        /* topic = */ "demo_java",
        /* value = */ "hello world"
    )

    KafkaFactory.producer<String, String>().use {
        send(producerRecord)
    }
}


