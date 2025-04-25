package config

import org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
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
    fun <K, V> producer(properties: Properties): KafkaProducer<K, V> {
        properties.putAll(KafkaConfig.producerProperties)
        return KafkaProducer<K, V>(properties)
    }

    /**
     * Creates a new consumer.
     */
    fun <K, V> consumer(
        topic: String,
        groupId: String,
        autoOffsetReset: AutoOffsetReset = AutoOffsetReset.EARLIEST,
        partitionAssignmentStrategy: PartitionAssignmentStrategy = PartitionAssignmentStrategy.Range,
        rebalanceListener: ConsumerRebalanceListener? = null,
    ): KafkaConsumer<K, V> {
        val properties: Properties = ConsumerPropertiesBuilder(groupId)
            .withAutoOffsetReset(autoOffsetReset)
            .withPartitionAssignmentStrategy(partitionAssignmentStrategy)
            .build()

        return KafkaConsumer<K, V>(properties).apply {
            val topics = listOf(topic)
            if (rebalanceListener != null) subscribe(topics, rebalanceListener)
            else subscribe(topics)
        }
    }
}

private class ConsumerPropertiesBuilder(val groupId: String) {
    private val properties = KafkaConfig.consumerProperties

    fun withAutoOffsetReset(autoOffsetReset: AutoOffsetReset): ConsumerPropertiesBuilder {
        properties.setProperty(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset.value)
        return this
    }

    fun withPartitionAssignmentStrategy(partitionAssignmentStrategy: PartitionAssignmentStrategy): ConsumerPropertiesBuilder {
        properties.setProperty(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, partitionAssignmentStrategy.value)
        return this
    }

    fun build(): Properties {
        return properties.apply {
            setProperty(GROUP_ID_CONFIG, groupId)
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