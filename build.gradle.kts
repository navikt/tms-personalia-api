import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer
import com.expediagroup.graphql.plugin.gradle.graphql
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)
    kotlin("plugin.serialization").version(Kotlin.version)

    id(GraphQL.pluginId) version GraphQL.version
    id(Shadow.pluginId) version (Shadow.version)
    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    implementation(JacksonDatatype.datatypeJsr310)
    implementation(Kotlinx.coroutines)
    implementation(Kotlinx.htmlJvm)
    implementation(KotlinLogging.logging)
    implementation(Ktor2.Server.auth)
    implementation(Ktor2.Server.authJwt)
    implementation(Ktor2.Server.defaultHeaders)
    implementation(Ktor2.Client.apache)
    implementation(Ktor2.Client.contentNegotiation)
    implementation(Ktor2.Serialization.kotlinX)
    implementation(Ktor2.Server.contentNegotiation)
    implementation(Ktor2.Server.netty)
    implementation(DittNAVCommonLib.utils)
    implementation(TmsKtorTokenSupport.tokendingsExchange)
    implementation(TmsKtorTokenSupport.tokenXValidation)
    implementation(Logstash.logbackEncoder)
    implementation(GraphQL.kotlinKtorClient)
    implementation(NAV.tokenValidatorKtor)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(Prometheus.logback)

    testImplementation(Junit.api)
    testImplementation(Ktor.clientMock)
    testImplementation(Ktor.clientMockJvm)
    testImplementation(Kluent.kluent)
    testImplementation(Mockk.mockk)
    testImplementation(Jjwt.api)

    testRuntimeOnly(Bouncycastle.bcprovJdk15on)
    testRuntimeOnly(Jjwt.impl)
    testRuntimeOnly(Jjwt.jackson)
    testRuntimeOnly(Junit.engine)
}

application {
    mainClass.set("no.nav.personbruker.tms.personalia.api.config.AppKt")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed")
        }
    }
}

graphql {
    client {
        packageName = "no.nav.pdl.generated.dto"
        schemaFile = file("${project.projectDir}/src/main/resources/graphql/schema.graphql")
        queryFileDirectory = "${project.projectDir}/src/main/resources/graphql"
        serializer = GraphQLSerializer.KOTLINX
    }
}

apply(plugin = Shadow.pluginId)
