package no.nav.personbruker.tms.personalia.api.personalia

import com.expediagroup.graphql.client.types.GraphQLClientResponse
import no.nav.pdl.generated.dto.HentNavn

fun PersonaliaNavn.toPersonaliaNavnDTO(): PersonaliaNavnDTO {
    return PersonaliaNavnDTO(
        navn = concatenateToNavn(fornavn, mellomnavn, etternavn)
    )
}

fun toPersonaliaNavn(result: GraphQLClientResponse<HentNavn.Result>) =
    result.data?.hentPerson?.navn?.map {
        PersonaliaNavn(
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
