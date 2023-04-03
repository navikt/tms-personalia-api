package no.nav.personbruker.tms.personalia.api.ident

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import no.nav.personbruker.tms.personalia.api.config.tokenXUser

fun Route.identApi() {

    get("/ident") {
            call.respond(HttpStatusCode.OK, IdentDto(tokenXUser.ident))
    }

}

@Serializable
data class IdentDto(
    val ident: String
)
