package no.nav.personbruker.tms.personalia.api.ident

import no.nav.tms.token.support.tokenx.validation.user.TokenXUser

class IdentService {

    fun extractIdent(user: TokenXUser): Ident {
        return Ident(user.ident)
    }
}
