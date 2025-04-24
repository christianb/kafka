package kafka.course.demo

import demo.config.KafkaConfig
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.lang.Exception

fun main() {
    KafkaProducer<String, String>(KafkaConfig.properties).use {
        for (i in 0 until 10) {
            val producerRecord = ProducerRecord<String, String>(
                /* topic = */ "demo_java",
                /* value = */ "hello world $i"
            )
            send(producerRecord, callback)
        }
    }
}

val callback = object : Callback {
    override fun onCompletion(metadata: RecordMetadata, exception: Exception?) {
        if (exception != null) {
            log.error("Error while producing", exception)
            return
        }

        log.info(
            "received metadata \n"
                    + "Topic: ${metadata.topic()} \n"
                    + "Partition: ${metadata.partition()} \n"
                    + "Offset: ${metadata.offset()} \n"
                    + "Timestamp: ${metadata.timestamp()}"
        )
    }
}