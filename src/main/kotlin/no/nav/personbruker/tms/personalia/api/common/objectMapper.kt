package no.nav.personbruker.tms.personalia.api.common

import com.fasterxml.jackson.databind.ObjectMapper

inline fun <reified T> ObjectMapper.readObject(string: String): T = readValue(string, T::class.java)
