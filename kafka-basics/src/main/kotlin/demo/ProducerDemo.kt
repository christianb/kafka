package kafka.course.demo

import demo.config.KafkaConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

fun main() {
    val producerRecord = ProducerRecord<String, String>(
        /* topic = */ "demo_java",
        /* value = */ "hello world"
    )

    KafkaProducer<String, String>(KafkaConfig.properties).use {
        send(producerRecord)
    }
}


