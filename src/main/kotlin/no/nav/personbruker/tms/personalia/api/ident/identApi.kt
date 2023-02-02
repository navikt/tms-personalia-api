package no.nav.personbruker.tms.personalia.api.ident

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import mu.KotlinLogging
import no.nav.personbruker.tms.personalia.api.common.ExceptionResponseHandler
import no.nav.personbruker.tms.personalia.api.config.tokenXUser
import no.nav.tms.token.support.tokenx.validation.user.TokenXUserFactory

fun Route.identApi() {

    val log = KotlinLogging.logger {}

    get("/ident") {
        try {
            call.respond(HttpStatusCode.OK, tokenXUser.ident)

        } catch (exception: Exception) {
            val errorCode = ExceptionResponseHandler.logExceptionAndDecideErrorResponseCode(log, exception)
            call.respond(errorCode)
        }
    }

}
