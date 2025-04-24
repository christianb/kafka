package demo

import config.KafkaFactory
import config.log
import config.use
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.lang.Exception

fun main() {
    KafkaFactory.producer<String, String>().use {
        repeat(3) {
            for (i in 0 until 10) {
                val key = "id_$i"
                val producerRecord = ProducerRecord(
                    /* topic = */ "kotlin_demo",
                    /* key = */ key,
                    /* value = */ "hello world $i"
                )
                producerRecord.key()
                send(producerRecord, callback(key))
            }
        }
    }
}

private fun callback(key: String) = object : Callback {
    override fun onCompletion(metadata: RecordMetadata, exception: Exception?) {
        if (exception != null) {
            log.error("Error while producing", exception)
            return
        }

        log.info("Key: $key | Partition: ${metadata.partition()}")
    }
}