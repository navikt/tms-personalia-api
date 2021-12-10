package no.nav.personbruker.tms.personalia.api.config

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import no.nav.personbruker.tms.personalia.api.health.HealthService
import no.nav.personbruker.tms.personalia.api.ident.IdentService
import no.nav.personbruker.tms.personalia.api.navn.NavnConsumer
import no.nav.personbruker.tms.personalia.api.navn.NavnService
import no.nav.tms.token.support.tokendings.exchange.TokendingsServiceBuilder
import java.net.URL

class ApplicationContext {
    private val environment = Environment()

    val httpClient = HttpClientBuilder.build()
    val healthService = HealthService(this)

    val tokendingsService = TokendingsServiceBuilder.buildTokendingsService(maxCachedEntries = 10000)
    val tokendingsTokenFetcher = TokendingsTokenFetcher(tokendingsService, environment.pdlClientId)

    val navnConsumer = NavnConsumer(GraphQLKtorClient(URL(environment.pdlUrl), httpClient), environment.pdlUrl)
    val navnService = NavnService(navnConsumer, tokendingsTokenFetcher)

    val identService = IdentService()

}
