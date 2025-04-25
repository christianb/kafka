package config

import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

object KafkaConfig {
    private const val SERVER_ADDRESS = "127.0.0.1:9092"

    val producerProperties = Properties().apply {
        setProperty(BOOTSTRAP_SERVERS_CONFIG, SERVER_ADDRESS) // connect unsecure to local host
        setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.qualifiedName)
        setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.qualifiedName)
    }
        get() = field.clone() as Properties

    val consumerProperties = Properties().apply {
        setProperty(BOOTSTRAP_SERVERS_CONFIG, SERVER_ADDRESS) // connect unsecure to local host
        setProperty(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.qualifiedName)
        setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.qualifiedName)
    }
        get() = field.clone() as Properties
}