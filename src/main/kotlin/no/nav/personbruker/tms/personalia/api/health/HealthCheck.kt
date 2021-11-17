package no.nav.personbruker.tms.personalia.api.health

interface HealthCheck {

    suspend fun status(): HealthStatus

}
