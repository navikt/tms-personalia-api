package no.nav.personbruker.tms.personalia.api.personalia

fun PersonaliaNavn.toPersonaliaNavnDTO(): PersonaliaNavnDTO {
    return PersonaliaNavnDTO(
        navn = concatenateToNavn(fornavn, mellomnavn, etternavn)
    )
}

private fun concatenateToNavn(fornavn: String?, mellomnavn: String?, etternavn: String?): String {
    return listOf(fornavn, mellomnavn, etternavn)
        .filter { navn -> !navn.isNullOrBlank() }
        .joinToString(" ")
}
