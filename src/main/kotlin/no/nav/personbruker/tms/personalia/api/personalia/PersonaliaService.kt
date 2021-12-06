package no.nav.personbruker.tms.personalia.api.personalia

import no.nav.personbruker.tms.personalia.api.config.TokendingsTokenFetcher
import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class PersonaliaService(
    private val personaliaConsumer: PersonaliaConsumer,
    private val tokendingsTokenFetcher: TokendingsTokenFetcher
) {

    suspend fun fetchNavn(user: TokenXUser): PersonaliaNavn {
        val token = tokendingsTokenFetcher.getPdlToken(user.tokenString)
        val personInfo = personaliaConsumer.fetchNavn(user.ident, token)

        return personInfo.navn.first()
    }

    fun extractIdent(user: TokenXUser): PersonaliaIdent {
        return PersonaliaIdent(user.ident)
    }
}
