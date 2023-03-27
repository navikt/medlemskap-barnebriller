package no.nav.medlemskap.barnebriller.config

import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import java.time.Duration

val retryConfig: RetryConfig = RetryConfig
    .custom<RetryConfig>()
    .maxAttempts(1)
    .waitDuration(Duration.ofSeconds(1))
    .retryExceptions(RuntimeException::class.java)
    .build()

val retryRegistry: RetryRegistry = RetryRegistry.of(retryConfig)
