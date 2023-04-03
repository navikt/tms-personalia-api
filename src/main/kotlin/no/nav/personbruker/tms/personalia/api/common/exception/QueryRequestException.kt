package no.nav.personbruker.tms.personalia.api.common.exception

open class QueryRequestException(message: String, cause: Throwable) : Exception(message, cause)

class QueryResponseException(message: String) : Exception(message)

class TransformationException(message: String) : Exception(message)

