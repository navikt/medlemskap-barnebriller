package no.nav.medlemskap.barnebriller

import no.nav.medlemskap.barnebriller.config.Environment
import no.nav.medlemskap.barnebriller.nais.createHttpServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    Application().start()
}

class Application(private val env: Environment = System.getenv(),
                 ) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    fun start() {
        log.info("Start application")
        createHttpServer().start(wait = true)
    }
}