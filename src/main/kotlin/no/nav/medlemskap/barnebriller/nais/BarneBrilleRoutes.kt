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
import no.nav.medlemskap.barnebriller.rest.logStatistics
import no.nav.medlemskap.barnebriller.service.BarneBrilleRequestService
import no.nav.medlemskap.barnebriller.service.LovmeService
import no.nav.medlemskap.barnebriller.service.pdl.PdlService
import java.util.*

private val logger = KotlinLogging.logger { }
private val secureLogger = KotlinLogging.logger("tjenestekall")
fun Routing.barneBrilleRoutes() {
    val barneBrilleRequestService = BarneBrilleRequestService(PdlService(),LovmeService())
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
            val callerPrincipal: JWTPrincipal = call.authentication.principal()!!
            println(callerPrincipal)
            val azp = callerPrincipal.payload.getClaim("azp").asString()
            secureLogger.info("barnebriller : EvalueringRoute: azp-claim i principal-token: {}", azp)
            val callId = call.callId ?: UUID.randomUUID().toString()
            val request = call.receive<Request>()
            val response = barneBrilleRequestService.handle(request,callId)
            runCatching { response.logStatistics(secureLogger,callId,request.fnr)}
                .onFailure {
                logger.warn("klarte ikke å logge statestikk for kall med id $callId")
            }
            call.respond(HttpStatusCode.OK, response)

        }
        post("/") {
            val callerPrincipal: JWTPrincipal = call.authentication.principal()!!
            println(callerPrincipal)
            val azp = callerPrincipal.payload.getClaim("azp").asString()
            secureLogger.info("/ : EvalueringRoute: azp-claim i principal-token: {}", azp)
            val callId = call.callId ?: UUID.randomUUID().toString()
            val request = call.receive<Request>()
            val response = barneBrilleRequestService.handle(request,callId)
            call.respond(HttpStatusCode.OK, response)

        }
    }


}