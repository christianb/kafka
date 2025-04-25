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
        val openSearchUri = URI.create(EnvConfig.OPEN_SEARCH_URL)
        val userInfo = openSearchUri.getUserInfo()

        if (userInfo == null) throw SecurityException("unsafe")

        val auth: Array<String?> = userInfo.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val credentialsProvider = BasicCredentialsProvider().apply {
            setCredentials(AuthScope.ANY, UsernamePasswordCredentials(auth[0], auth[1]))
        }

        val host = HttpHost(openSearchUri.host, openSearchUri.port, openSearchUri.scheme)
        val restClientBuilder = RestClient.builder(host)
            .setHttpClientConfigCallback { builder ->
                builder
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy())
            }
        return RestHighLevelClient(restClientBuilder)
    }
}
