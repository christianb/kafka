package demo.wikimedia

import com.launchdarkly.eventsource.EventSource
import config.KafkaFactory
import config.WIKIMEDIA_TOPIC
import config.use
import org.apache.kafka.clients.producer.ProducerConfig.*
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit

fun main() {
    val properties = Properties().apply {
        // high throughput producer config
        setProperty(LINGER_MS_CONFIG, "20")
        setProperty(BATCH_SIZE_CONFIG, (32 * 1024).toString())
        setProperty(COMPRESSION_TYPE_CONFIG, "lz4")
    }

    KafkaFactory.producer<String, String>(properties).use {
        val eventHandler = WikimediaChangeHandler(
            producer = this,
            topic = WIKIMEDIA_TOPIC
        )
        val url = "https://stream.wikimedia.org/v2/stream/recentchange"
        val eventSource = EventSource.Builder(eventHandler, URI.create(url)).build()

        eventSource.start()

        // produce for 10 minutes and block the program until
        TimeUnit.MINUTES.sleep(10)
    }
}