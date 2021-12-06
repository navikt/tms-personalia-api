package no.nav.personbruker.tms.personalia.api.personalia

import no.nav.personbruker.tms.personalia.api.config.TokendingsTokenFetcher
import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class PersonaliaService(
    private val personaliaConsumer: PersonaliaConsumer,
    private val tokendingsTokenFetcher: TokendingsTokenFetcher
) {

    suspend fun fetchNavn(user: TokenXUser): PersonaliaNavnDTO {
        val token = tokendingsTokenFetcher.getPdlToken(user.tokenString)
        val external = personaliaConsumer.fetchNavn(user.ident, token)
        val externalNavn = external.navn.first()

        return externalNavn.toPersonaliaNavnDTO()
    }

    fun extractIdent(user: TokenXUser): PersonaliaIdent {
        return PersonaliaIdent(user.ident)
    }
}
