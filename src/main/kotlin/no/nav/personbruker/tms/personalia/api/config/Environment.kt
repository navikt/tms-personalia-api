package no.nav.personbruker.tms.personalia.api.config

import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar

data class Environment(
    val pdlUrl: String = getEnvVar("PDL_BASE_URL"),
    val pdlClientId: String = getEnvVar("PDL_CLIENT_ID")
)
