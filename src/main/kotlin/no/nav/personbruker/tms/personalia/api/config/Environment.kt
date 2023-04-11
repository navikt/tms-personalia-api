package no.nav.personbruker.tms.personalia.api.config

import no.nav.personbruker.dittnav.common.util.config.StringEnvVar

data class Environment(
    val pdlUrl: String = StringEnvVar.getEnvVar("PDL_BASE_URL"),
    val pdlClientId: String = StringEnvVar.getEnvVar("PDL_CLIENT_ID")
)