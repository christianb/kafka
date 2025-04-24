package demo.wikimedia

import com.launchdarkly.eventsource.EventSource
import config.KafkaFactory
import config.use
import java.net.URI
import java.util.concurrent.TimeUnit

fun main() {
    KafkaFactory.producer<String, String>().use {

        val eventHandler = WikimediaChangeHandler(
            producer = this,
            topic = "wikimedia.recentchange"
        )
        val url = "https://stream.wikimedia.org/v2/stream/recentchange"
        val eventSource = EventSource.Builder(eventHandler, URI.create(url)).build()

        eventSource.start()

        // produce for 10 minutes and block the program until
        TimeUnit.MINUTES.sleep(10)
    }
}