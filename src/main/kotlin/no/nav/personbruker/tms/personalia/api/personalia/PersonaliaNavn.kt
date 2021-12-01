package no.nav.personbruker.tms.personalia.api.personalia

import kotlinx.serialization.Serializable

@Serializable
data class PersonaliaNavn(
    val fornavn: String?,
    val mellomnavn: String?,
    val etternavn: String?
)
