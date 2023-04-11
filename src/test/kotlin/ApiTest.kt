import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import no.nav.personbruker.tms.personalia.api.navn.NavnConsumer
import no.nav.personbruker.tms.personalia.api.personaliaApi
import no.nav.tms.token.support.tokendings.exchange.TokendingsService
import no.nav.tms.token.support.tokenx.validation.mock.SecurityLevel
import no.nav.tms.token.support.tokenx.validation.mock.installTokenXAuthMock
import org.junit.jupiter.api.Test
import java.net.URL

class ApiTest {
    private val objectmapper = ObjectMapper()

    @Test
    fun ident() = testApplication {
        testApi()
        client.get("/tms-personalia-api/ident").apply {
            status shouldBe HttpStatusCode.OK
            objectmapper.readTree(bodyAsText())["ident"].asText() shouldBe "12345678910"
        }

    }

    @Test
    fun navn() = testApplication {
        testApi()
        externalServices {
            hosts("https://pdl.test") {
                routing {
                    route("") {
                        post {
                            call.respondBytes(
                                contentType = ContentType.Application.Json,
                                provider = { dummyPdlResult.toByteArray() })
                        }
                    }
                }
            }
        }

        client.get("/tms-personalia-api/navn").apply {
            status shouldBe HttpStatusCode.OK
            objectmapper.readTree(bodyAsText())["navn"].asText() shouldBe "Packup Your Troubles"
        }
    }


    private fun ApplicationTestBuilder.testApi(httpClient: HttpClient = createClient { }) = application {
        personaliaApi(httpClient = httpClient, navnConsumer = NavnConsumer(
            client = GraphQLKtorClient(url = URL("https://pdl.test"), httpClient = httpClient),
            pdlUrl = "https://pdl.test",
            tokendingsService = mockk<TokendingsService>().also {
                coEvery {
                    it.exchangeToken(
                        any(),
                        any()
                    )
                } returns "<dummytoken>"
            },
            pdlClientId = "pdl.test.id"
        ), authConfig = {
            installTokenXAuthMock {
                setAsDefault = true
                staticUserPid = "12345678910"
                staticSecurityLevel = SecurityLevel.LEVEL_4
                alwaysAuthenticated = true
            }
        })
    }

    private val dummyPdlResult = """
    {
      "data": {
        "hentPerson": {
          "navn": [
            {
              "fornavn": "Packup",
              "mellomnavn": "Your",
              "etternavn": "Troubles"
            }
          ]
        }
      }
    }
""".trimMargin()
}


