package no.nav.personbruker.tms.personalia.api

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import io.ktor.client.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.tms.personalia.api.config.Environment
import no.nav.personbruker.tms.personalia.api.config.HttpClientBuilder
import no.nav.personbruker.tms.personalia.api.config.confiureStatusPages
import no.nav.personbruker.tms.personalia.api.config.healthApi
import no.nav.personbruker.tms.personalia.api.config.jsonConfig
import no.nav.personbruker.tms.personalia.api.navn.NavnConsumer
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder
import no.nav.tms.token.support.tokenx.validation.installTokenXAuth
import no.nav.tms.token.support.tokenx.validation.user.TokenXUserFactory
import java.net.URL

fun main() {
    val environment = Environment()

    val httpClient = HttpClientBuilder.build()
    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService(maxCachedEntries = 10000)
    val navnConsumer = NavnConsumer(
        client = GraphQLKtorClient(url = URL(environment.pdlUrl), httpClient = httpClient),
        pdlUrl = environment.pdlUrl,
        tokendingsService = tokendingsService,
        pdlClientId = environment.pdlClientId
    )

    embeddedServer(Netty, port = 8080) {
        personaliaApi(
            httpClient,
            navnConsumer,
            tokenxAuth()
        )
    }.start(wait = true)
}

fun Application.personaliaApi(
    httpClient: HttpClient,
    navnConsumer: NavnConsumer,
    authConfig: Application.() -> Unit
) {
    DefaultExports.initialize()

    authConfig()

    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    install(StatusPages) {
        this.confiureStatusPages()
    }

    routing {
        route("/tms-personalia-api") {
            healthApi()

            authenticate {
                api(navnConsumer)
            }
        }
    }

    configureShutdownHook(httpClient)
}


fun tokenxAuth(): Application.() -> Unit = {
    installTokenXAuth {
        setAsDefault = true
    }
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}
