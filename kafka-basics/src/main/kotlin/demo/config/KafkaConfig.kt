package demo.config

import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

object KafkaConfig {
    val properties = Properties().apply {
        setProperty("bootstrap.servers", "127.0.0.1:9092") // connect unsecure to local host

        setProperty("key.serializer", StringSerializer::class.qualifiedName)
        setProperty("value.serializer", StringSerializer::class.qualifiedName)
    }
}