package no.nav.personbruker.tms.personalia.api.common.exception

open class QueryRequestException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
