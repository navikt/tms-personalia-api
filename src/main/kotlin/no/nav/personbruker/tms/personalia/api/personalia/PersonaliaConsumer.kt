package no.nav.personbruker.tms.personalia.api.personalia

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.types.GraphQLClientError
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.ktor.client.request.*
import no.nav.pdl.generated.dto.HentNavn
import org.slf4j.LoggerFactory

class PersonaliaConsumer (
    private val client: GraphQLKtorClient,
    private val pdlUrl: String
) {

    val log = LoggerFactory.getLogger(PersonaliaConsumer::class.java)

    suspend fun fetchNavn(ident: String, token: String): GraphQLClientResponse<HentNavn.Result> {
        val hentNavnQuery = HentNavn(HentNavn.Variables(ident = ident))

        val response: GraphQLClientResponse<HentNavn.Result> = client.execute(hentNavnQuery) {
            url(pdlUrl)
            bearerHeader(token)
            temaHeader("GEN")
        }

        checkForErrors(response)

        return response
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
        throw Exception("Feil under henting av navn")
    }

}
