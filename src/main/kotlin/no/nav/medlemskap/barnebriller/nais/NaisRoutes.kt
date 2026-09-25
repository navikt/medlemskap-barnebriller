package no.nav.medlemskap.barnebriller.nais


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
fun Routing.naisRoutes() {

    get("/isAlive") {
        if (true) {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        } else {
            call.respondText("I am Not alive :(", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
    get("/isReady") {
        call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
    }
    get("/metrics") {
        call.respondTextWriter(ContentType.parse("text/plain; version=0.0.4; charset=utf-8")) {
            writeMetrics004(this, Metrics.registry)
        }
    }
}