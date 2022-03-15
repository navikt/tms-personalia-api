package no.nav.personbruker.tms.personalia.api.dittnav

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.tms.personalia.api.common.ExceptionResponseHandler
import no.nav.personbruker.tms.personalia.api.navn.NavnService
import org.slf4j.LoggerFactory

fun Route.dittnavApi() {

    val log = LoggerFactory.getLogger(NavnService::class.java)

    get("/test") {
        try {
            call.respond(HttpStatusCode.OK, "It worked!")

        } catch (exception: Exception) {
            val errorCode = ExceptionResponseHandler.logExceptionAndDecideErrorResponseCode(log, exception)
            call.respond(errorCode)
        }
    }

}
