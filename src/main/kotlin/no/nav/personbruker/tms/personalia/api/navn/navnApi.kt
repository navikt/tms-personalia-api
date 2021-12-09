package no.nav.personbruker.tms.personalia.api.navn

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.tms.personalia.api.common.ExceptionResponseHandler
import no.nav.personbruker.tms.personalia.api.config.tokenXUser
import org.slf4j.LoggerFactory

fun Route.navnApi(navnService: NavnService) {

    val log = LoggerFactory.getLogger(NavnService::class.java)

    get("/navn") {
        try {
            val result = navnService.fetchNavn(tokenXUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            val errorCode = ExceptionResponseHandler.logExceptionAndDecideErrorResponseCode(log, exception)
            call.respond(errorCode)
        }
    }

}
