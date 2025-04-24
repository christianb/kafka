package kafka.course.demo

import demo.config.KafkaConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.Properties

fun <K, V> KafkaProducer<K, V>.use(block: KafkaProducer<K, V>.() -> Unit) {
    block(this)
    flush()
    close()
}

fun <K, V> KafkaConsumer<K, V>.use(block: KafkaConsumer<K, V>.() -> Unit) {
    block(this)
    close()
}

object KafkaFactory {
    fun <K, V> producer(): KafkaProducer<K, V> = KafkaProducer<K, V>(KafkaConfig.producerProperties)

    fun <K, V> consumer(topic: String, groupId: String, autoOffsetReset: AutoOffsetReset = AutoOffsetReset.EARLIEST): KafkaConsumer<K, V> {
        val properties: Properties = ConsumerPropertiesBuilder(groupId)
            .withAutoOffsetReset(autoOffsetReset)
            .build()

        return KafkaConsumer<K, V>(properties).also {
            it.subscribe(listOf(topic))
        }
    }
}

class ConsumerPropertiesBuilder(val groupId: String) {
    private val properties = KafkaConfig.consumerProperties

    fun withAutoOffsetReset(autoOffsetReset: AutoOffsetReset): ConsumerPropertiesBuilder {
        properties.setProperty("auto.offset.reset", autoOffsetReset.value)
        return this
    }

    fun build(): Properties {
        return properties.apply {
            setProperty("group.id", groupId)
        }
    }
}

enum class AutoOffsetReset(val value: String) {
    NONE("none"),
    EARLIEST("earliest"),
    LATEST("latest")
}