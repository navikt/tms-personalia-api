package no.nav.personbruker.tms.personalia.api.config

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
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
