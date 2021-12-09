package no.nav.personbruker.tms.personalia.api.navn

import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.pdl.generated.dto.HentNavn

fun Navn.toInternalNavnDTO(): NavnDTO {
    return NavnDTO(
        navn = concatenateToNavn(fornavn, mellomnavn, etternavn)
    )
}

fun toExternalNavn(result: GraphQLClientResponse<HentNavn.Result>) =
    result.data?.hentPerson?.navn?.map {
        Navn(
            fornavn = it.fornavn,
            mellomnavn = it.mellomnavn,
            etternavn = it.etternavn
        )
    } ?: emptyList()

private fun concatenateToNavn(fornavn: String?, mellomnavn: String?, etternavn: String?): String {
    return listOf(fornavn, mellomnavn, etternavn)
        .filter { navn -> !navn.isNullOrBlank() }
        .joinToString(" ")
}
