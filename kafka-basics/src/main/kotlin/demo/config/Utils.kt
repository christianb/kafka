package kafka.course.demo

import demo.config.KafkaConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.errors.WakeupException
import java.util.Properties

fun <K, V> KafkaProducer<K, V>.use(block: KafkaProducer<K, V>.() -> Unit) {
    block(this)
    flush()
    close()
}

fun <K, V> KafkaConsumer<K, V>.use(block: KafkaConsumer<K, V>.() -> Unit) {
    val mainThread = Thread.currentThread()
    val shutdownHook = object : Thread() {
        override fun run() {
            log.info("Detected a shutdown ...")
            wakeup()
            try {
                mainThread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)

    try {
        block(this)
    } catch (_: WakeupException) {
        log.info("Consumer is starting to shut down ...")
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        close()
        log.info("Consumer is now gracefully shut down :)")
    }
}

object KafkaFactory {
    fun <K, V> producer(): KafkaProducer<K, V> = KafkaProducer<K, V>(KafkaConfig.producerProperties)

    fun <K, V> consumer(
        topic: String,
        groupId: String,
        autoOffsetReset: AutoOffsetReset = AutoOffsetReset.EARLIEST
    ): KafkaConsumer<K, V> {
        val properties: Properties = ConsumerPropertiesBuilder(groupId)
            .withAutoOffsetReset(autoOffsetReset)
            .build()

        val consumer = KafkaConsumer<K, V>(properties)
        consumer.subscribe(listOf(topic))
        return consumer
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