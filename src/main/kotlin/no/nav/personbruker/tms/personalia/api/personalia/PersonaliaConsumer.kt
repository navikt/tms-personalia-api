package no.nav.personbruker.tms.personalia.api.personalia

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.personbruker.tms.personalia.api.common.readObject
import no.nav.personbruker.tms.personalia.api.config.JsonDeserialize.objectMapper
import no.nav.personbruker.tms.personalia.api.common.exception.PdlAuthenticationException
import no.nav.personbruker.tms.personalia.api.common.exception.PdlException
import no.nav.personbruker.tms.personalia.api.personalia.pdl.PDLErrorType
import no.nav.personbruker.tms.personalia.api.personalia.pdl.PdlErrorResponse
import no.nav.personbruker.tms.personalia.api.personalia.pdl.PdlPersonInfo
import no.nav.personbruker.tms.personalia.api.personalia.pdl.PdlResponse
import no.nav.personbruker.tms.personalia.api.personalia.query.*
import org.slf4j.LoggerFactory

class PersonaliaConsumer (
    private val client: HttpClient,
    private val pdlUrl: String
) {

    val log = LoggerFactory.getLogger(PersonaliaConsumer::class.java)

    suspend fun fetchNavn(ident: String, token: String): PdlPersonInfo {
        val request = createNavnRequest(ident)

        return postPersonQuery(request, token).let { responseBody ->
            parsePdlResponse(responseBody)
        }
    }

    private suspend fun postPersonQuery(request: NavnRequest, token: String): String {
        return try {
            client.post {
                url(pdlUrl)
                contentType(ContentType.Application.Json)
                bearerHeader(token)
                temaHeader("GEN")
                body = request
            }
        } catch (e: Exception) {
            throw PdlException("Feil ved kontakt mot pdl-api", e)
        }
    }

    private fun parsePdlResponse(json: String): PdlPersonInfo {
        return try {
            val personResponse: PdlResponse = objectMapper.readObject(json)
            personResponse.data.person
        } catch (e: Exception) {
            handleErrorResponse(json)
        }
    }

    private fun handleErrorResponse(json: String): Nothing {
        try {
            val errorResponse: PdlErrorResponse = objectMapper.readObject(json)
            logErrorResponse(errorResponse)
            throwAppropriateException(errorResponse)
        } catch (e: Exception) {
            throw PdlException("Feil ved deserialisering av svar fra pdl. Response-body lengde [${json.length}]", e)
        }
    }

    private fun throwAppropriateException(response: PdlErrorResponse): Nothing {
        val firstError = response.errors.first().errorType

        if (firstError == PDLErrorType.NOT_AUTHENTICATED) {
            throw PdlAuthenticationException("Fikk autentiseringsfeil mot PDL [$firstError]")
        } else {
            throw PdlException("Fikk feil fra pdl med type [$firstError]")
        }
    }

    private fun logErrorResponse(response: PdlErrorResponse) {
        val firstError = response.errors.first().errorType

        when (firstError) {
            PDLErrorType.NOT_FOUND -> log.warn("Fant ikke bruker i PDL.")
            PDLErrorType.NOT_AUTHENTICATED -> log.warn("Autentiseringsfeil mot PDL. Feil i brukertoken eller systemtoken.")
            PDLErrorType.ABAC_ERROR -> log.warn("Systembruker har ikke tilgang til opplysning")
            PDLErrorType.UNKNOWN_ERROR -> log.warn("Ukjent feil mot PDL")
        }
    }

}
