package no.nav.personbruker.tms.personalia.api.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.*
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object HttpClientBuilder {

    fun build(httpClientEngine: HttpClientEngine = Apache.create()): HttpClient {
        return HttpClient(httpClientEngine) {
            install(ContentNegotiation) {
                json(jsonConfig())
            }
            install(HttpTimeout)
        }
    }
}

fun jsonConfig() = Json {
    this.ignoreUnknownKeys = true
    this.encodeDefaults = true
}

fun HttpRequestBuilder.bearerHeader(token: String, headerKey: String = HttpHeaders.Authorization) {
    header(headerKey, "Bearer $token")
}

fun HttpRequestBuilder.temaHeader() {
    header("Tema", "GEN")
}
