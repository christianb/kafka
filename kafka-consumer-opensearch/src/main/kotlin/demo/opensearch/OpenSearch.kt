package demo.opensearch

import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient
import java.net.URI

object OpenSearch {

    val client = createOpenSearchClient()

    private fun createOpenSearchClient(): RestHighLevelClient {
        val connString = EnvConfig.OPEN_SEARCH_URL
        val userInfo = URI.create(connString).getUserInfo()

        return if (userInfo == null) {
            // REST client without security
            RestHighLevelClient(RestClient.builder(HttpHost(
                URI.create(connString).host,
                URI.create(connString).port, "http")))
        } else {
            // REST client with security
            val auth: Array<String?> = userInfo.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val credentialsProvider = BasicCredentialsProvider().apply {
                setCredentials(AuthScope.ANY, UsernamePasswordCredentials(auth[0], auth[1]))
            }

            RestHighLevelClient(
                RestClient.builder(HttpHost(
                    URI.create(connString).host, URI.create(connString).port,
                    URI.create(connString).scheme))
                    .setHttpClientConfigCallback { builder ->
                        builder
                            .setDefaultCredentialsProvider(credentialsProvider)
                            .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy())
                    }
            )
        }
    }
}
