package no.nav.personbruker.tms.personalia.api.common

import io.ktor.http.*
import no.nav.personbruker.tms.personalia.api.common.exception.QueryRequestException
import no.nav.personbruker.tms.personalia.api.common.exception.QueryResponseException
import no.nav.personbruker.tms.personalia.api.common.exception.TransformationException
import org.slf4j.Logger

object ExceptionResponseHandler {
    fun logExceptionAndDecideErrorResponseCode(log: Logger, exception: Exception): HttpStatusCode {
        return when (exception) {
            is QueryResponseException -> {
                val errorCode = HttpStatusCode.ServiceUnavailable
                val msg = "Klarte ikke å hente data. Returnerer feilkoden $errorCode. $exception"
                log.warn(msg, exception)
                errorCode
            }
            is QueryRequestException -> {
                val errorCode = HttpStatusCode.ServiceUnavailable
                val msg = "Klarte ikke å hente data. Returnerer feilkoden $errorCode. $exception"
                log.warn(msg, exception)
                errorCode
            }
            is TransformationException -> {
                val errorCode = HttpStatusCode.InternalServerError
                val msg = "Mottok verdi som ikke kunne konverteres til den interne-modellen. Returnerer feilkoden $errorCode. $exception"
                log.warn(msg, exception)
                errorCode
            }
            else -> {
                val errorCode = HttpStatusCode.InternalServerError
                val msg = "Ukjent feil oppstod. Returnerer feilkoden $errorCode. $exception"
                log.error(msg, exception)
                errorCode
            }
        }
    }
}
