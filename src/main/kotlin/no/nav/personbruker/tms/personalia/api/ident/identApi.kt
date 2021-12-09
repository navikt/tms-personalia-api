package no.nav.personbruker.tms.personalia.api.ident

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.tms.personalia.api.common.ExceptionResponseHandler
import no.nav.personbruker.tms.personalia.api.config.tokenXUser
import no.nav.personbruker.tms.personalia.api.navn.NavnService
import org.slf4j.LoggerFactory

fun Route.identApi(identService: IdentService) {

    val log = LoggerFactory.getLogger(NavnService::class.java)

    get("/ident") {
        try {
            val result = identService.extractIdent(tokenXUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            val errorCode = ExceptionResponseHandler.logExceptionAndDecideErrorResponseCode(log, exception)
            call.respond(errorCode)
        }
    }

}
