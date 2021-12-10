package no.nav.personbruker.tms.personalia.api.navn

import kotlinx.serialization.Serializable

@Serializable
data class NavnDTO(
    val navn: String?
)
