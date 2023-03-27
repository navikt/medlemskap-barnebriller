package no.nav.medlemskap.barnebriller.clients


import io.github.resilience4j.retry.Retry
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.medlemskap.barnebriller.clients.medloppslag.MedlOppslagRequest
import no.nav.medlemskap.barnebriller.http.runWithRetryAndMetrics
import no.nav.medlemskap.barnebriller.clients.azuread.AzureAdClient



class MedlOppslagClient(
    private val baseUrl: String,
    private val azureAdClient: AzureAdClient,
    private val httpClient: HttpClient,
    private val retry: Retry? = null
): LovmeAPI {

    override suspend fun vurderMedlemskap(medlOppslagRequest: MedlOppslagRequest, callId: String): String {
        val token = azureAdClient.hentTokenScopetMotMedlemskapOppslag()
        return runWithRetryAndMetrics("MEDL-OPPSLAG", "vurdermedlemskap", retry) {
            httpClient.post {
                url("$baseUrl/kafka_v2")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                header("Nav-Call-Id", callId)
                header("X-Correlation-Id", callId)
                setBody(medlOppslagRequest)
            }.body()
        }
    }
    override suspend fun vurderMedlemskapBomlo(medlOppslagRequest: MedlOppslagRequest, callId: String): String {
        val token = azureAdClient.hentTokenScopetMotMedlemskapOppslag()
        return runWithRetryAndMetrics("MEDL-OPPSLAG", "vurdermedlemskap", retry) {
            httpClient.post {
                url("$baseUrl/")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                header("Nav-Call-Id", callId)
                header("X-Correlation-Id", callId)
                setBody(medlOppslagRequest)
            }.body()
        }
    }
}

interface LovmeAPI{
    suspend fun vurderMedlemskap(medlOppslagRequest: MedlOppslagRequest, callId: String): String
    suspend fun vurderMedlemskapBomlo(medlOppslagRequest: MedlOppslagRequest, callId: String): String
}
