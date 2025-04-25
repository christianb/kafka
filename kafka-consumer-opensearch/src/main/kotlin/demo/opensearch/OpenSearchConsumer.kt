package demo.opensearch

import config.log
import org.opensearch.client.RequestOptions.DEFAULT
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import java.io.IOException

private const val WIKIMEDIA_INDEX = "wikimedia"

fun main() {
    val client = OpenSearch.client

    val indicesClient = client.indices()

    try {
        val exists = indicesClient.exists(GetIndexRequest(WIKIMEDIA_INDEX), /* options = */ DEFAULT)
        if (!exists) {
            indicesClient.create(CreateIndexRequest(WIKIMEDIA_INDEX), /* options = */ DEFAULT)
            log.info("Wikimedia index created")
        } else log.info("Wikimedia index already exists")

    } catch (e: IOException) {
        log.error("error", e)
    } finally {
        client.close()
    }
}

