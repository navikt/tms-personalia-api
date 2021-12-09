package no.nav.personbruker.tms.personalia.api.personalia

import kotlinx.serialization.Serializable

@Serializable
data class NavnDTO(
    val navn: String?
)
