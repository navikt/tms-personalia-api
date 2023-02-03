package no.nav.personbruker.tms.personalia.api.config

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import io.ktor.client.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.tms.personalia.api.ident.identApi
import no.nav.personbruker.tms.personalia.api.navn.NavnConsumer
import no.nav.personbruker.tms.personalia.api.navn.NavnService
import no.nav.personbruker.tms.personalia.api.navn.navnApi
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder
import no.nav.tms.token.support.tokenx.validation.installTokenXAuth
import no.nav.tms.token.support.tokenx.validation.user.TokenXUserFactory
import java.net.URL

fun main() {
    val environment = Environment()

    val httpClient = HttpClientBuilder.build()

    val navnService = setupNavnService(httpClient, environment.pdlUrl, environment.pdlClientId)

    embeddedServer(Netty, port = environment.port) {
        personaliaApi(httpClient, navnService, tokenxAuth())
    }.start(wait = true)
}

fun Application.personaliaApi(
    httpClient: HttpClient,
    navnService: NavnService,
    authConfig: Application.() -> Unit
) {
    DefaultExports.initialize()

    authConfig()

    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    routing {
        route("/tms-personalia-api") {
            healthApi()

            authenticate {
                identApi()
                navnApi(navnService)
            }
        }
    }

    configureShutdownHook(httpClient)
}

fun tokenxAuth(): Application.() -> Unit  = {
    installTokenXAuth {
        setAsDefault = true
    }
}

fun setupNavnService(httpClient: HttpClient, pdlUrl: String, pdlClientId: String): NavnService {
    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService(maxCachedEntries = 10000)
    val tokendingsTokenFetcher = TokendingsTokenFetcher(tokendingsService, pdlClientId)

    val navnConsumer = NavnConsumer(GraphQLKtorClient(URL(pdlUrl), httpClient), pdlUrl)

    return NavnService(navnConsumer, tokendingsTokenFetcher)
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

val PipelineContext<*, ApplicationCall>.tokenXUser
    get() = TokenXUserFactory.createTokenXUser(call)
