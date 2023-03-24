package no.nav.medlemskap.barnebriller.nais


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import no.nav.medlemskap.barnebriller.clients.azuread.AzureAdClient
import no.nav.medlemskap.barnebriller.config.Configuration

private val logger = KotlinLogging.logger { }
private val secureLogger = KotlinLogging.logger("tjenestekall")
fun Routing.localRoutes() {

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




}