package no.nav.personbruker.tms.personalia.api.common.exception

class QueryResponseException: Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}
