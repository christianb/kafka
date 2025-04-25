package config

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.errors.WakeupException

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