package no.nav.personbruker.tms.personalia.api.personalia.query

class NavnRequest(val variables: QueryVariables) {

    val query: String
        get() = """
            query (${"$"}ident: ID!) {
                person: hentPerson(ident: ${"$"}ident) {
                     navn { fornavn, mellomnavn, etternavn }
                } 
            }
        """.compactJson()
}

fun createNavnRequest(ident: String): NavnRequest {
    return NavnRequest(QueryVariables(ident))
}

private fun String.compactJson(): String =
    trimIndent().replace("\r", " ").replace("\n", " ").replace("\\s+".toRegex(), " ")

