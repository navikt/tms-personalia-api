package no.nav.personbruker.tms.personalia.api.navn

import kotlinx.serialization.Serializable

@Serializable
data class Navn(
    val fornavn: String?,
    val mellomnavn: String?,
    val etternavn: String?
)
