package demo

import kafka.course.demo.KafkaFactory
import kafka.course.demo.use
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


