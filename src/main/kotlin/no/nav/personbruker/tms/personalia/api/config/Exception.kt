package no.nav.personbruker.tms.personalia.api.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

open class QueryRequestException(message: String, cause: Throwable) : Exception(message, cause)

class QueryResponseException(message: String) : Exception(message)

class TransformationException(message: String) : Exception(message)


fun StatusPagesConfig.configureStatusPages() {
    val secureLog = KotlinLogging.logger("secureLog")

    exception<Throwable> { call, cause ->
        when (cause) {
            is QueryResponseException -> {
                secureLog.warn(cause) { "Klarte ikke å hente data. ${cause.message}" }
                call.respond(HttpStatusCode.ServiceUnavailable)
            }
            is QueryRequestException -> {
                secureLog.warn(cause) { "Klarte ikke å hente data. ${cause.message}" }
                call.respond(HttpStatusCode.ServiceUnavailable)
            }
            is TransformationException -> {
                secureLog.warn(cause) { "Mottok verdi som ikke kunne konverteres til den interne-modellen. Returnerer feilkoden $cause" }
                call.respond(HttpStatusCode.InternalServerError)
            }
            else -> {
                secureLog.warn(cause) { "Ukjent feil: ${cause.message}" }
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}



