package no.nav.personbruker.tms.personalia.api.personalia

import no.nav.personbruker.tms.personalia.api.config.TokendingsTokenFetcher
import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class PersonaliaService(
    private val personaliaConsumer: PersonaliaConsumer,
    private val tokendingsTokenFetcher: TokendingsTokenFetcher
) {

    suspend fun fetchNavn(user: TokenXUser): PersonaliaNavnDTO {
        val token = tokendingsTokenFetcher.getPdlToken(user.tokenString)
        val response = personaliaConsumer.fetchNavn(user.ident, token)
        val external = toPersonaliaNavn(response).first()

        return external.toPersonaliaNavnDTO()
    }

    fun extractIdent(user: TokenXUser): PersonaliaIdent {
        return PersonaliaIdent(user.ident)
    }
}
