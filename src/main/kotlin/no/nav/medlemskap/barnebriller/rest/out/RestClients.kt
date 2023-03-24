package no.nav.medlemskap.barnebriller.rest.out



import no.nav.medlemskap.barnebriller.clients.azuread.AzureAdClient
import no.nav.medlemskap.barnebriller.clients.medloppslag.MedlOppslagClient
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.config.retryRegistry
import no.nav.medlemskap.barnebriller.http.cioHttpClient
import no.nav.medlemskap.barnebriller.service.pdl.PdlClient


class RestClients(
    private val configuration: Configuration
) {

    private val azureAdClient = AzureAdClient(configuration)
    private val pdlRetry = retryRegistry.retry("PDL")


    private val httpClient = cioHttpClient

   fun pdl(endpointBaseURl: String) = PdlClient(endpointBaseURl, azureAdClient, configuration.sts.username, httpClient, pdlRetry, configuration.register.pdlApiKey)
   fun lovme(endpointBaseURl: String) = MedlOppslagClient(endpointBaseURl, azureAdClient, httpClient, pdlRetry)

}
