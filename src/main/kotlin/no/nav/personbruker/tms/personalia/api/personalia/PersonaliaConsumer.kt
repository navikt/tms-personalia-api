package no.nav.personbruker.tms.personalia.api.personalia

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.types.GraphQLClientError
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.ktor.client.request.*
import no.nav.pdl.generated.dto.HentNavn
import no.nav.personbruker.tms.personalia.api.common.exception.QueryRequestException
import no.nav.personbruker.tms.personalia.api.common.exception.QueryResponseException
import org.slf4j.LoggerFactory

class PersonaliaConsumer (
    private val client: GraphQLKtorClient,
    private val pdlUrl: String
) {

    val log = LoggerFactory.getLogger(PersonaliaConsumer::class.java)

    suspend fun fetchNavn(ident: String, token: String): GraphQLClientResponse<HentNavn.Result> {
        val response: GraphQLClientResponse<HentNavn.Result> = sendQuery(ident, token)

        checkForErrors(response)
        return response
    }

    private suspend fun sendQuery(ident: String, token: String): GraphQLClientResponse<HentNavn.Result> {
        try {
            val hentNavnQuery = HentNavn(HentNavn.Variables(ident = ident))

            return client.execute(hentNavnQuery) {
                url(pdlUrl)
                bearerHeader(token)
                temaHeader("GEN")
            }
        } catch (e: Exception) {
            throw QueryRequestException("Feil under sending av graphql spørringen", e)
        }

    }

    private fun checkForErrors(response: GraphQLClientResponse<HentNavn.Result>) {
        response.errors?.let { errors ->
            if (errors.isNotEmpty()) {
                handleErrors(errors)
            }
        }
    }

    private fun handleErrors(errors: List<GraphQLClientError>) {
        log.warn("Feil i GraphQL-responsen: $errors")
        throw QueryResponseException("Feil i responsen under henting av navn")
    }

}
