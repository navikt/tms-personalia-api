package no.nav.personbruker.tms.personalia.api.navn

import no.nav.personbruker.tms.personalia.api.config.TokendingsTokenFetcher
import no.nav.personbruker.tms.personalia.api.ident.Ident
import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class NavnService(
    private val navnConsumer: NavnConsumer,
    private val tokendingsTokenFetcher: TokendingsTokenFetcher
) {

    suspend fun fetchNavn(user: TokenXUser): NavnDTO {
        val token = tokendingsTokenFetcher.getPdlToken(user.tokenString)
        val response = navnConsumer.fetchNavn(user.ident, token)
        val external = toExternalNavn(response).first()

        return external.toInternalNavnDTO()
    }

}
