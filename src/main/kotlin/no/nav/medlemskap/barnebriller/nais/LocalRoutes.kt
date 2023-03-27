package no.nav.medlemskap.barnebriller.nais


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import no.nav.medlemskap.barnebriller.clients.azuread.AzureAdClient
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.rest.Request
import no.nav.medlemskap.barnebriller.service.BarneBrilleRequestService
import no.nav.medlemskap.barnebriller.service.LovmeService
import no.nav.medlemskap.barnebriller.service.pdl.PdlService
import java.util.*

private val logger = KotlinLogging.logger { }
private val secureLogger = KotlinLogging.logger("tjenestekall")
fun Routing.localRoutes() {
    val barneBrilleRequestService = BarneBrilleRequestService(PdlService(),LovmeService())
        get("/token") {
            val token = AzureAdClient(Configuration()).hentTokenScopetMotSelf()
            try{
                call.respondText(token.token, ContentType.Text.Plain, HttpStatusCode.OK)
            }
            catch (t:Throwable){
                call.respond(t.stackTrace)
            }
        }
        get("/token_oppslag") {
            val token = AzureAdClient(Configuration()).hentTokenScopetMotMedlemskapOppslag()
            try{
                call.respondText(token.token, ContentType.Text.Plain, HttpStatusCode.OK)
             }
            catch (t:Throwable){
                call.respond(t.stackTrace)
            }
        }
        post("/barnebrillerlocal") {
            val request = call.receive<Request>()
            val response = barneBrilleRequestService.handle(request, UUID .randomUUID().toString())
            call.respond(HttpStatusCode.OK, response)
        }

}