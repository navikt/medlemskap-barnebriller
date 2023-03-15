package no.nav.medlemskap.barnebriller.nais

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Timer
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter

object Metrics {
    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM)

    fun incReceivedTotal(count: Int = 1) =
        receivedTotal.inc(count.toDouble())

    fun incProcessedTotal(count: Int = 1) =
        processedTotal.inc(count.toDouble())

    fun incSuccessfulLovmePosts(count: Int = 1) =
        utforteLovmeKallOK.inc(count.toDouble())



    private val receivedTotal: Counter = Counter.build()
        .name("medlemskap_barnebriller_api_call_received")
        .help("Totalt mottatte medlemskap-barnebriller meldinger")
        .register()

    private val processedTotal: Counter = Counter.build()
        .name("medlemskap_barnebrillerr_api_call_completed")
        .help("Totalt prosesserte api kall")
        .register()
    private val utforteLovmeKallOK: Counter = Counter.build()
        .name("medlemskap_barnebriller_successful_lovme_posts_counter")
        .help("Vellykede kall mot Lovme behandlet")
        .register()

    fun clientCounter(service: String?, operation: String?, status: String): io.micrometer.core.instrument.Counter =
        io.micrometer.core.instrument.Counter
            .builder("client_calls_total")
            .tags("service", service ?: "UKJENT", "operation", operation ?: "UKJENT", "status", status)
            .description("counter for failed or successful calls to other services")
            .register(Metrics.globalRegistry)

    fun clientTimer(service: String?, operation: String?): Timer =
        Timer.builder("client_calls_latency")
            .tags("service", service ?: "UKJENT", "operation", operation ?: "UKJENT")
            .description("latency for calls to other services")
            .publishPercentileHistogram()
            .register(Metrics.globalRegistry)

}
