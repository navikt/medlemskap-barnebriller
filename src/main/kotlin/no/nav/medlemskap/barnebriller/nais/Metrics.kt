package no.nav.medlemskap.barnebriller.nais

import io.micrometer.core.instrument.Timer
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

object Metrics {
    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    fun incReceivedTotal(count: Int = 1) =
        receivedTotal.increment(count.toDouble())

    fun incProcessedTotal(count: Int = 1) =
        processedTotal.increment(count.toDouble())

    fun incSuccessfulLovmePosts(count: Int = 1) =
        utforteLovmeKallOK.increment(count.toDouble())



    private val receivedTotal = io.micrometer.core.instrument.Counter
        .builder("medlemskap_barnebriller_api_call_received")
        .description("Totalt mottatte medlemskap-barnebriller meldinger")
        .register(registry)

    private val processedTotal = io.micrometer.core.instrument.Counter
        .builder("medlemskap_barnebrillerr_api_call_completed")
        .description("Totalt prosesserte api kall")
        .register(registry)

    private val utforteLovmeKallOK = io.micrometer.core.instrument.Counter
        .builder("medlemskap_barnebriller_successful_lovme_posts_counter")
        .description("Vellykede kall mot Lovme behandlet")
        .register(registry)

    fun clientCounter(service: String?, operation: String?, status: String): io.micrometer.core.instrument.Counter =
        io.micrometer.core.instrument.Counter
            .builder("client_calls_total")
            .tags("service", service ?: "UKJENT", "operation", operation ?: "UKJENT", "status", status)
            .description("counter for failed or successful calls to other services")
            .register(registry)

    fun clientTimer(service: String?, operation: String?): Timer =
        Timer.builder("client_calls_latency")
            .tags("service", service ?: "UKJENT", "operation", operation ?: "UKJENT")
            .description("latency for calls to other services")
            .publishPercentileHistogram()
            .register(registry)

}
