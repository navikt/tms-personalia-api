package no.nav.personbruker.tms.personalia.api.config

import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import mu.KotlinLogging


fun StatusPagesConfig.confiureStatusPages() {
    val secureLog = KotlinLogging.logger("secureLog")

    exception<Throwable> { call, cause ->
        when (cause) {
            is QueryResponseException -> {
                secureLog.warn("Klarte ikke å hente data. ${cause.message}", cause.stackTrace)
                call.respond(HttpStatusCode.ServiceUnavailable)
            }
            is QueryRequestException -> {
                secureLog.warn("Klarte ikke å hente data. ${cause.message}", cause.stackTrace)
                call.respond(HttpStatusCode.ServiceUnavailable)
            }
            is TransformationException -> {
                secureLog.warn(
                    "Mottok verdi som ikke kunne konverteres til den interne-modellen. Returnerer feilkoden $cause",
                    cause.stackTrace
                )
                call.respond(HttpStatusCode.InternalServerError)
            }
            else -> {
                secureLog.warn("Ukjent feil: ${cause.message}", cause.stackTrace)
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

open class QueryRequestException(message: String, cause: Throwable) : Exception(message, cause)

class QueryResponseException(message: String) : Exception(message)

class TransformationException(message: String) : Exception(message)

