package no.nav.medlemskap.barnebriller.nais


import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.medlemskap.barnebriller.config.AzureAdOpenIdConfiguration
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.config.JwtConfig
import no.nav.medlemskap.barnebriller.config.JwtConfig.Companion.REALM
import no.nav.medlemskap.barnebriller.config.getAadConfig
import no.nav.medlemskap.barnebriller.http.MDC_CALL_ID
import no.nav.medlemskap.barnebriller.rest.objectMapper
import org.slf4j.event.Level
import java.io.Writer
import java.util.*

fun createHttpServer() = embeddedServer(Netty, applicationEngineEnvironment {
    val useAuthentication: Boolean = true
    val configuration: Configuration = Configuration()
    val azureAdOpenIdConfiguration: AzureAdOpenIdConfiguration = getAadConfig(configuration.azureAd)

    connector { port = 8080 }
    module {

        install(CallId) {
            header(MDC_CALL_ID)
            generate { UUID.randomUUID().toString() }
        }
        install(CallLogging) {
            level = Level.INFO
            callIdMdc(MDC_CALL_ID)
        }


        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(objectMapper))
        }

        install(MicrometerMetrics) {
            registry = Metrics.registry
        }

        if (useAuthentication) {
            //logger.info { "Installerer authentication" }
            install(Authentication) {
                jwt("azureAuth") {
                    val jwtConfig = JwtConfig(configuration, azureAdOpenIdConfiguration)
                    realm = REALM
                    verifier(jwtConfig.jwkProvider, azureAdOpenIdConfiguration.issuer)
                    validate { credentials ->
                        jwtConfig.validate(credentials)
                    }
                }
            }
        } else {
            println("setter opp basic auth for lokal testing")
            install(Authentication) {
                basic() {
                    realm = "Access to the '/' path"
                    validate { credentials ->
                        if (credentials.name == "foo" && credentials.password == "bar") {
                            UserIdPrincipal(credentials.name)
                        } else {
                            null
                        }
                    }
                }
            }

        }

        routing {
            naisRoutes()
            barneBrilleRoutes()
        }
    }
})
suspend fun writeMetrics004(writer: Writer, registry: PrometheusMeterRegistry) {
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            TextFormat.write004(writer, registry.prometheusRegistry.metricFamilySamples())
        }
    }
}





