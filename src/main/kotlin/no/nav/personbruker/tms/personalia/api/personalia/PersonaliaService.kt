package no.nav.personbruker.tms.personalia.api.personalia

import no.nav.personbruker.tms.personalia.api.config.TokendingsTokenFetcher
import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class PersonaliaService(
    private val personaliaConsumer: PersonaliaConsumer,
    private val tokendingsTokenFetcher: TokendingsTokenFetcher
) {

    suspend fun fetchNavn(user: TokenXUser): NavnDTO {
        val token = tokendingsTokenFetcher.getPdlToken(user.tokenString)
        val response = personaliaConsumer.fetchNavn(user.ident, token)
        val external = toExternalNavn(response).first()

        return external.toInternalNavnDTO()
    }

    fun extractIdent(user: TokenXUser): Ident {
        return Ident(user.ident)
    }
}
