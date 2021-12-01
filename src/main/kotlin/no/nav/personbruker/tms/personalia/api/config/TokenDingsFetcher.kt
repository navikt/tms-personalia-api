package no.nav.personbruker.tms.personalia.api.config

import no.nav.tms.token.support.tokendings.exchange.TokendingsService

class TokendingsTokenFetcher(
    private val tokendingsService: TokendingsService,
    private val pdlClientId: String
) {
    suspend fun getPdlToken(userToken: String): String {
        return tokendingsService.exchangeToken(userToken, pdlClientId)
    }
}
