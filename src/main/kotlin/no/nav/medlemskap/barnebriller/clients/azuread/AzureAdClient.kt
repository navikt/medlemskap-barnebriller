package no.nav.medlemskap.barnebriller.clients.azuread

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.http.apacheHttpClient
import no.nav.medlemskap.barnebriller.security.Token


class AzureAdClient(private val configuration: Configuration) {

    suspend fun hentTokenScopetMotMedlemskapOppslag(): Token {
        val formUrlEncode = listOf(
            "client_id" to configuration.azureAd.clientId,
            "scope" to "api://${configuration.register.medlemskapOppslagClientID}/.default",
            "client_secret" to configuration.azureAd.clientSecret,
            "grant_type" to "client_credentials"
        ).formUrlEncode()

        return apacheHttpClient.post {
            url(configuration.azureAd.tokenEndpoint)
            setBody(TextContent(formUrlEncode, ContentType.Application.FormUrlEncoded))
        }.body()
    }
    suspend fun hentTokenScopetMotPDL(): Token {
        val formUrlEncode = listOf(
            "client_id" to configuration.azureAd.clientId,
            "scope" to configuration.register.scope_pdl,
            "client_secret" to configuration.azureAd.clientSecret,
            "grant_type" to "client_credentials"
        ).formUrlEncode()

        return apacheHttpClient.post {
            url(configuration.azureAd.tokenEndpoint)
            setBody(TextContent(formUrlEncode, ContentType.Application.FormUrlEncoded))
        }.body()
    }
    suspend fun hentTokenScopetMotSelf(): Token {
        val formUrlEncode = listOf(
            "client_id" to configuration.azureAd.clientId,
            "scope" to "api://${configuration.azureAd.clientId}/.default",
            "client_secret" to configuration.azureAd.clientSecret,
            "grant_type" to "client_credentials"
        ).formUrlEncode()

        return apacheHttpClient.post {
            url(configuration.azureAd.tokenEndpoint)
            setBody(TextContent(formUrlEncode, ContentType.Application.FormUrlEncoded))
        }.body()
    }
}
