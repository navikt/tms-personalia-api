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

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

repositories {
    mavenLocal()
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    implementation(JacksonDatatype.datatypeJsr310)
    implementation(Kotlinx.coroutines)
    implementation(KotlinLogging.logging)
    implementation(Ktor.Server.auth)
    implementation(Ktor.Server.defaultHeaders)
    implementation(Ktor.Client.apache)
    implementation(Ktor.Client.contentNegotiation)
    implementation(Ktor.Serialization.kotlinX)
    implementation(Ktor.Server.contentNegotiation)
    implementation(Ktor.Server.netty)
    implementation(Ktor.Server.statusPages)
    implementation(DittNAVCommonLib.utils)
    implementation(TmsKtorTokenSupport.tokendingsExchange)
    implementation(TmsKtorTokenSupport.tokenXValidation)
    implementation(Logstash.logbackEncoder)
    implementation(KotlinLogging.logging)
    implementation(GraphQL.kotlinKtorClient)
    implementation(Prometheus.common)
    implementation(Prometheus.hotspot)
    implementation(TmsCommonLib.commonLib)

    testImplementation(Junit.api)
    testImplementation(Ktor.Test.serverTestHost)
    testImplementation(TmsKtorTokenSupport.tokenXValidationMock)
    testImplementation(Mockk.mockk)
    testImplementation(Kotest.runnerJunit5)
    testImplementation(Kotest.assertionsCore)

    testRuntimeOnly(Junit.engine)
}

application {
    mainClass.set("no.nav.personbruker.tms.personalia.api.AppKt")
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
