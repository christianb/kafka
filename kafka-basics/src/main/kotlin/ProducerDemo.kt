package kafka.course

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

fun main() {
    val properties = Properties().apply {
        setProperty("bootstrap.servers", "127.0.0.1:9092") // connect unsecure to local host

        setProperty("key.serializer", StringSerializer::class.qualifiedName)
        setProperty("value.serializer", StringSerializer::class.qualifiedName)
    }

    val producerRecord = ProducerRecord<String, String>(
        /* topic = */ "demo_java",
        /* value = */ "hello world"
    )

    val producer = KafkaProducer<String, String>(properties)
    producer.send(producerRecord)
    producer.flush() // synchronous operation that tells the producer to send all data and block until done
    producer.close() // close would also call flush()
}


