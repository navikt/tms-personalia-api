package no.nav.personbruker.tms.personalia.api.health

import no.nav.personbruker.tms.personalia.api.config.ApplicationContext

class HealthService(private val applicationContext: ApplicationContext) {

    suspend fun getHealthChecks(): List<HealthStatus> {
        return emptyList()
    }
}
