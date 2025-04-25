package demo.opensearch

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import config.AutoOffsetReset
import config.KafkaFactory
import config.WIKIMEDIA_TOPIC
import config.log
import config.use
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.opensearch.OpenSearchStatusException
import org.opensearch.action.bulk.BulkRequest
import org.opensearch.action.index.IndexRequest
import org.opensearch.client.RequestOptions.DEFAULT
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import org.opensearch.common.xcontent.XContentType
import java.io.IOException
import java.time.Duration
import java.util.Properties

private const val WIKIMEDIA_INDEX = "wikimedia"
private const val OPENSEARCH_CONSUMER_ID = "opensearch-consumer"

fun main() {
    val openSearchClient = OpenSearch.client

    try {
        val indicesClient = openSearchClient.indices()
        val exists = indicesClient.exists(GetIndexRequest(WIKIMEDIA_INDEX), /* options = */ DEFAULT)
        if (!exists) {
            indicesClient.create(CreateIndexRequest(WIKIMEDIA_INDEX), /* options = */ DEFAULT)
            log.info("Wikimedia index created")
        } else log.info("Wikimedia index already exists")

    } catch (e: IOException) {
        log.error("error", e)
        openSearchClient.close()
    }

    val properties = Properties().apply {
        setProperty(GROUP_ID_CONFIG, OPENSEARCH_CONSUMER_ID)
        setProperty(AUTO_OFFSET_RESET_CONFIG, AutoOffsetReset.LATEST.value)
        setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    }

    KafkaFactory.consumer<String, String>(topic = WIKIMEDIA_TOPIC, properties).use {
        while (true) {
            val consumerRecords: ConsumerRecords<String, String> = poll(Duration.ofSeconds(3))
            val bulkRequest = BulkRequest()

            for (record: ConsumerRecord<String, String> in consumerRecords) {
                if (!record.value().startsWith("{")) {
                    log.error("record.value() is not valid json: ${record.value()}")
                    continue
                }

                val json = record.value().removeLogParams()
                val wikimediaId = deserializeId(json)
                val indexRequest: IndexRequest = IndexRequest(WIKIMEDIA_INDEX)
                    .source(/* source = */ json, /* mediaType = */ XContentType.JSON)
                    .id(wikimediaId)

                bulkRequest.add(indexRequest)
            }

            if (bulkRequest.numberOfActions() == 0) {
                log.info("no data received ...")
                continue
            }

            try {
                val response = openSearchClient.bulk(bulkRequest, DEFAULT)
                log.info("Inserted ${response.items.size} records")
                // commit offsets after the batch has been consumed
                commitSync()
            } catch (e: OpenSearchStatusException) {
                log.error("error", e)
            } catch (e: IOException) {
                log.error("error", e)
            }
        }
    }
}

private fun deserializeId(json: String): String {
    return try {
        JsonParser.parseString(json).asJsonObject
            .getAsJsonObject("meta")
            .get("id").asString
    } catch (e: Exception) {
        log.error("could not parse Json: $json")
        null!!
    }
}

private fun String.removeLogParams(): String {
    val jsonElement: JsonElement = JsonParser.parseString(this)
    if (jsonElement.isJsonObject) jsonElement.asJsonObject.remove("log_params")
    return Gson().toJson(jsonElement)
}