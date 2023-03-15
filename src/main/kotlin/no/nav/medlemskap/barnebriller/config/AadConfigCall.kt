package no.nav.medlemskap.barnebriller.config

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.call.*
import io.ktor.client.request.*


import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.medlemskap.barnebriller.http.apacheHttpClient


@JsonIgnoreProperties(ignoreUnknown = true)
data class AzureAdOpenIdConfiguration(
    @JsonProperty("jwks_uri")
    val jwksUri: String,
    @JsonProperty("issuer")
    val issuer: String,
    @JsonProperty("token_endpoint")
    val tokenEndpoint: String,
    @JsonProperty("authorization_endpoint")
    val authorizationEndpoint: String
)

private val logger = KotlinLogging.logger { }

fun getAadConfig(azureAdConfig: Configuration.AzureAd): AzureAdOpenIdConfiguration = runBlocking {
    apacheHttpClient.get("${azureAdConfig.authorityEndpoint}/${azureAdConfig.tenant}/v2.0/.well-known/openid-configuration")
        .body<AzureAdOpenIdConfiguration>().also { logger.info { it } }
}
