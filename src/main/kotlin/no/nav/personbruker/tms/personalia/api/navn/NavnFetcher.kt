package no.nav.personbruker.tms.personalia.api.navn

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.ktor.client.request.*
import io.ktor.http.*
import mu.KotlinLogging
import no.nav.pdl.generated.dto.HentNavn
import no.nav.personbruker.tms.personalia.api.config.QueryRequestException
import no.nav.personbruker.tms.personalia.api.config.QueryResponseException
import no.nav.personbruker.tms.personalia.api.config.TransformationException
import no.nav.tms.token.support.tokendings.exchange.TokendingsService
import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class NavnConsumer(
    private val client: GraphQLKtorClient,
    private val pdlUrl: String,
    private val tokendingsService: TokendingsService,
    private val pdlClientId: String
) {

    private val log = KotlinLogging.logger {}

    suspend fun fetchNavn(user: TokenXUser): NavnDTO {
        val token = tokendingsService.exchangeToken(user.tokenString, pdlClientId)
        val response: GraphQLClientResponse<HentNavn.Result> = sendQuery(user.ident, token)
        checkForErrors(response)
        return getNavnFromGraphQl(response).toInternalNavnDTO()
    }

    private suspend fun sendQuery(ident: String, token: String): GraphQLClientResponse<HentNavn.Result> {
        try {
            val hentNavnQuery = HentNavn(HentNavn.Variables(ident = ident))

            return client.execute(hentNavnQuery) {
                url(pdlUrl)
                header(HttpHeaders.Authorization, "Bearer $token")
                header("Tema", "GEN")
            }
        } catch (e: Exception) {
            throw QueryRequestException("Feil under sending av graphql spørringen", e)
        }
    }

    private fun checkForErrors(response: GraphQLClientResponse<HentNavn.Result>) {
        response.errors?.let { errors ->
            if (errors.isNotEmpty()) {
                log.warn("Feil i GraphQL-responsen: $errors")
                throw QueryResponseException("Feil i responsen under henting av navn")
            }
        }
    }
}

fun getNavnFromGraphQl(result: GraphQLClientResponse<HentNavn.Result>) =
    result.data?.hentPerson?.navn?.map {
        Navn(
            fornavn = it.fornavn,
            mellomnavn = it.mellomnavn,
            etternavn = it.etternavn
        )
    }?.first() ?: throw TransformationException("Klarte ikke å transformere responsen til Navn")