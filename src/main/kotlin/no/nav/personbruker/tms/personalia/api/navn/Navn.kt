package no.nav.personbruker.tms.personalia.api.navn

import kotlinx.serialization.Serializable

@Serializable
data class Navn(
    val fornavn: String?,
    val mellomnavn: String?,
    val etternavn: String?
) {
    fun toInternalNavnDTO(): NavnDTO = NavnDTO(navn = concatenateToNavn(fornavn, mellomnavn, etternavn))

    private fun concatenateToNavn(fornavn: String?, mellomnavn: String?, etternavn: String?): String {
        return listOf(fornavn, mellomnavn, etternavn)
            .filter { navn -> !navn.isNullOrBlank() }
            .joinToString(" ")
    }
}

@Serializable
data class NavnDTO(
    val navn: String?
)



