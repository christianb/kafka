package demo.opensearch

import com.google.gson.JsonParser
import config.AutoOffsetReset
import config.KafkaFactory
import config.WIKIMEDIA_TOPIC
import config.log
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.opensearch.action.index.IndexRequest
import org.opensearch.client.RequestOptions.DEFAULT
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import org.opensearch.common.xcontent.XContentType
import java.io.IOException
import java.time.Duration

private const val WIKIMEDIA_INDEX = "wikimedia"
private const val OPENSEARCH_CONSUMER_ID = "opensearch-consumer"

fun main() {
    val openSearchClient = OpenSearch.client
    val indicesClient = openSearchClient.indices()

    val consumer = KafkaFactory.consumer<String, String>(
        topic = WIKIMEDIA_TOPIC,
        groupId = OPENSEARCH_CONSUMER_ID,
        autoOffsetReset = AutoOffsetReset.LATEST
    )

    try {
        val exists = indicesClient.exists(GetIndexRequest(WIKIMEDIA_INDEX), /* options = */ DEFAULT)
        if (!exists) {
            indicesClient.create(CreateIndexRequest(WIKIMEDIA_INDEX), /* options = */ DEFAULT)
            log.info("Wikimedia index created")
        } else log.info("Wikimedia index already exists")

    } catch (e: IOException) {
        log.error("error", e)
        openSearchClient.close()
    }

    while (true) {
        val consumerRecords: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(3))
        log.info("received ${consumerRecords.count()} records")

        for (record: ConsumerRecord<String, String> in consumerRecords) {
            val wikimediaId = deserializeId(record.value())
            val indexRequest: IndexRequest = IndexRequest(WIKIMEDIA_INDEX)
                .source(/* source = */ record.value(), /* mediaType = */ XContentType.JSON)
                .id(wikimediaId)

            try {
                val response = openSearchClient.index(indexRequest, /* options = */ DEFAULT)
                log.info("Inserted document into OpenSearch: ${response.id}")
            } catch (e: IOException) {
                log.error("error", e)
            }
        }
    }
}

private fun deserializeId(json: String): String {
    return JsonParser.parseString(json).asJsonObject
        .get("meta").asJsonObject
        .get("id").asString
}