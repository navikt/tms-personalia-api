package no.nav.personbruker.tms.personalia.api.navn

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.personbruker.tms.personalia.api.config.tokenXUser

fun Route.navnApi(navnService: NavnService) {

    get("/navn") {
        val result = navnService.fetchNavn(tokenXUser)
        call.respond(HttpStatusCode.OK, result)
    }

}
