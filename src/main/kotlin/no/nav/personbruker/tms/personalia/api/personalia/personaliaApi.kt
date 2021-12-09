package no.nav.personbruker.tms.personalia.api.personalia

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.tms.personalia.api.common.ExceptionResponseHandler
import no.nav.personbruker.tms.personalia.api.config.tokenXUser
import org.slf4j.LoggerFactory

fun Route.personaliaApi(personaliaService: PersonaliaService) {

    val log = LoggerFactory.getLogger(PersonaliaService::class.java)

    get("/navn") {
        try {
            val result = personaliaService.fetchNavn(tokenXUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            val errorCode = ExceptionResponseHandler.logExceptionAndDecideErrorResponseCode(log, exception)
            call.respond(errorCode)
        }
    }

    get("/ident") {
        try {
            val result = personaliaService.extractIdent(tokenXUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            val errorCode = ExceptionResponseHandler.logExceptionAndDecideErrorResponseCode(log, exception)
            call.respond(errorCode)
        }
    }
}
