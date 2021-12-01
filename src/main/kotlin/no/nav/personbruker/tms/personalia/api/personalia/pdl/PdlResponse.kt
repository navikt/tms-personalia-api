package no.nav.personbruker.tms.personalia.api.personalia.pdl

data class PdlResponse(
    val data: PdlData
)

data class PdlData (
    val person: PdlPersonInfo
)
