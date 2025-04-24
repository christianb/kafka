package demo.wikimedia

import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.MessageEvent
import config.log
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord

class WikimediaChangeHandler(
    private val producer: Producer<String, String>,
    private val topic: String,
) : EventHandler {

    override fun onOpen() {
        // no implementation
    }

    override fun onClosed() {
        producer.close()
    }

    override fun onMessage(event: String, messageEvent: MessageEvent) {
        log.info("onMessage: ${messageEvent.data}")
        producer.send(ProducerRecord(topic, messageEvent.data))
    }

    override fun onComment(comment: String) {
        // no implementation
    }

    override fun onError(t: Throwable) {
        log.error("Error in stream reading", t)
    }
}