package config

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.RangeAssignor
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.Properties

object KafkaFactory {
    /**
     * Creates a new producer.
     *
     * @param [properties] additional properties. You do not need to incl [KafkaConfig.producerProperties]
     */
    fun <K, V> producer(properties: Properties = Properties()): KafkaProducer<K, V> {
        properties.putAll(KafkaConfig.producerProperties)
        return KafkaProducer<K, V>(properties)
    }

    /**
     * Creates a new consumer.
     */
    fun <K, V> consumer(
        topic: String,
        properties: Properties = Properties(),
        rebalanceListener: ConsumerRebalanceListener? = null,
    ): KafkaConsumer<K, V> {
        properties.putAll(KafkaConfig.consumerProperties)
        return KafkaConsumer<K, V>(properties).apply {
            val topics = listOf(topic)
            if (rebalanceListener != null) subscribe(topics, rebalanceListener) else subscribe(topics)
        }
    }
}

enum class AutoOffsetReset(val value: String) {
    NONE("none"),
    EARLIEST("earliest"),
    LATEST("latest")
}

enum class PartitionAssignmentStrategy(val value: String) {
    Range(RangeAssignor::class.qualifiedName!!),
    CooperativeSticky(CooperativeStickyAssignor::class.qualifiedName!!)
}

enum class Acks(val value: Int) {
    No(0),
    LeaderOnly(1),
    All(-1)
}