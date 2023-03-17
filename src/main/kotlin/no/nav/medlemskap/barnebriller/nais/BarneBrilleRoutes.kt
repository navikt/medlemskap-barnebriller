package no.nav.medlemskap.barnebriller.nais


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import no.nav.medlemskap.barnebriller.rest.Request
import java.util.*

private val logger = KotlinLogging.logger { }
private val secureLogger = KotlinLogging.logger("tjenestekall")
fun Routing.barneBrilleRoutes() {
    authenticate("azureAuth") {
        get("/deepPing") {
            val callerPrincipal: JWTPrincipal = call.authentication.principal()!!
            val azp = callerPrincipal.payload.getClaim("azp").asString()
            secureLogger.info("EvalueringRoute: azp-claim i principal-token: {}", azp)
            val callId = call.callId ?: UUID.randomUUID().toString()
            logger.info("kall autentisert, url : /deepPing",
                kv("callId", callId))
            try{
                call.respondText("deepPing ok!", ContentType.Text.Plain, HttpStatusCode.OK)
            }
            catch (t:Throwable){
                call.respond(t.stackTrace)
            }
        }
        post("/barnebriller") {
            val request = call.receive<Request>()
            call.respond(HttpStatusCode.OK, request)

        }
    }


}