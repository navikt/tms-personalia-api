package no.nav.personbruker.tms.personalia.api.personalia

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

fun HttpRequestBuilder.bearerHeader(token: String, headerKey: String = HttpHeaders.Authorization) {
    header(headerKey, "Bearer $token")
}

fun HttpRequestBuilder.temaHeader(temakode: String) {
    header("Tema", temakode)
}
