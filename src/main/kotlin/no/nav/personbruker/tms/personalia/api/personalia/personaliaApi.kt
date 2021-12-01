package no.nav.personbruker.tms.personalia.api.personalia

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import no.nav.personbruker.tms.personalia.api.config.tokenXUser
import org.slf4j.LoggerFactory

fun Route.personaliaApi(personaliaService: PersonaliaService) {

    val log = LoggerFactory.getLogger(PersonaliaService::class.java)

    get("/navn") {
        try {
            val result = personaliaService.fetchNavn(tokenXUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
            log.warn("Klarte ikke å hente navn. $exception.message", exception)
        }
    }

    get("/ident") {
        try {
            val result = personaliaService.extractIdent(tokenXUser)
            call.respond(HttpStatusCode.OK, result)

        } catch (exception: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
            log.warn("Klarte ikke å hente ident. $exception.message", exception)
        }
    }
}
