package kafka.course.demo

import org.apache.kafka.clients.producer.KafkaProducer

fun <T>KafkaProducer<T, T>.use(block: KafkaProducer<T, T>.() -> Unit) {
    block(this)
    flush()
    close()
}