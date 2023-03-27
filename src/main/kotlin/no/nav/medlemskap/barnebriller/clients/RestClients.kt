package no.nav.medlemskap.barnebriller.clients

import no.nav.medlemskap.barnebriller.http.cioHttpClient
import no.nav.medlemskap.barnebriller.config.retryRegistry
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.clients.azuread.AzureAdClient


class RestClients(
    private val azureAdClient: AzureAdClient,
    private val configuration: Configuration
) {

    private val medlRetry = retryRegistry.retry("MEDL-OPPSLAG")
    private val sagaRetry = retryRegistry.retry("MEDL-SAGA")

    private val httpClient = cioHttpClient
    fun medlOppslag(endpointBaseUrl: String) = MedlOppslagClient(endpointBaseUrl, azureAdClient, httpClient, medlRetry)
}
