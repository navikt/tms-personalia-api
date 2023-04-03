package no.nav.personbruker.tms.personalia.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import no.nav.personbruker.tms.personalia.api.navn.NavnConsumer
import no.nav.tms.token.support.tokenx.validation.user.TokenXUserFactory


fun Route.api(navnConsumer: NavnConsumer) {

    get("/navn") {
        val result = navnConsumer.fetchNavn(tokenXUser)
        call.respond(HttpStatusCode.OK, result)
    }

    get("/ident") {
        call.respond(HttpStatusCode.OK, IdentDto(tokenXUser.ident))
    }
}


@Serializable
data class IdentDto(
    val ident: String
)

val PipelineContext<*, ApplicationCall>.tokenXUser
    get() = TokenXUserFactory.createTokenXUser(call)
