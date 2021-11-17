package no.nav.personbruker.tms.personalia.api.config

import no.nav.personbruker.tms.personalia.api.health.HealthService

class ApplicationContext {

    val httpClient = HttpClientBuilder.build()
    val healthService = HealthService(this)

}
