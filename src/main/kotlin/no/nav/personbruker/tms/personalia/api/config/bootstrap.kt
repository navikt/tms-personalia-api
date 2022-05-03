package no.nav.personbruker.tms.personalia.api.config

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.tms.personalia.api.health.healthApi
import no.nav.personbruker.tms.personalia.api.ident.identApi
import no.nav.personbruker.tms.personalia.api.navn.navnApi
import no.nav.tms.token.support.tokenx.validation.installTokenXAuth
import no.nav.tms.token.support.tokenx.validation.user.TokenXUserFactory

fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {
    DefaultExports.initialize()

    install(DefaultHeaders)

    installTokenXAuth {
        setAsDefault = true
    }

    install(ContentNegotiation) {
        jackson {
            enableDittNavJsonConfig()
        }
    }

    routing {
        healthApi(appContext.healthService)

        authenticate {
            identApi(appContext.identService)
            navnApi(appContext.navnService)
        }
    }

    configureShutdownHook(appContext.httpClient)
}

private fun Application.configureShutdownHook(httpClient: HttpClient) {
    environment.monitor.subscribe(ApplicationStopping) {
        httpClient.close()
    }
}

val PipelineContext<*, ApplicationCall>.tokenXUser
    get() = TokenXUserFactory.createTokenXUser(call)
