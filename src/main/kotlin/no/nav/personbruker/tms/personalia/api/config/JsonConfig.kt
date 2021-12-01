package no.nav.personbruker.tms.personalia.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.features.json.*

fun buildJsonSerializer(): JacksonSerializer {
    return JacksonSerializer {
        enableDittNavJsonConfig()
    }
}

fun ObjectMapper.enableDittNavJsonConfig() {
    registerKotlinModule()
    registerModule(JavaTimeModule())
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}

object JsonDeserialize {
    val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        registerModule(JavaTimeModule())
    }
}
