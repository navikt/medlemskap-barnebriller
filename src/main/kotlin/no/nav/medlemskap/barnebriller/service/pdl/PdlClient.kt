package no.nav.medlemskap.barnebriller.service.pdl

import com.expediagroup.graphql.client.serialization.types.KotlinxGraphQLResponse
import com.expediagroup.graphql.client.types.GraphQLClientError
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.github.resilience4j.retry.Retry
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import mu.KotlinLogging
import no.nav.medlemskap.barnebriller.clients.azuread.AzureAdClient
import no.nav.medlemskap.barnebriller.http.runWithRetryAndMetrics
import no.nav.medlemskap.barnebriller.pdl.generated.HentPerson
import no.nav.medlemskap.barnebriller.pdl.generated.MedlemskapHentBarn
import no.nav.medlemskap.barnebriller.pdl.generated.MedlemskapHentVergeEllerForelder

class PdlClient(
    private val baseUrl: String,
    private val azureadClient: AzureAdClient,
    private val username: String,
    private val httpClient: HttpClient,
    private val retry: Retry? = null,
    private val pdlApiKey: String
) {
    companion object {
        private val logger = KotlinLogging.logger { }
    }

    suspend fun medlemskapHentBarn(fnr: String, callId: String): GraphQLClientResponse<MedlemskapHentBarn.Result> {

        return runWithRetryAndMetrics("PDL", "HentPerson", retry) {
            val token = azureadClient.hentTokenScopetMotPDL()
            val query = MedlemskapHentBarn(
                variables = MedlemskapHentBarn.Variables(
                    fnr
                )
            )
            val response: KotlinxGraphQLResponse<MedlemskapHentBarn.Result> = httpClient.post() {
                url(baseUrl)
                setBody(query)
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
                header("Nav-Call-Id", callId)
                header("TEMA", "HJE")
            }.body()

            if (!response.errors.isNullOrEmpty()) {

                logger.error("PDL response errors: ${response.errors}")
                // TODO: utfør feil håndtering. Gjøres utenfor denne koden?
            }
            val data = response.data
            val errors = response.errors
            if (errors != null) {
                when (errors!=null){
                    errors.inneholderKode(PdlNotFoundException.KODE) -> throw PdlNotFoundException()
                    errors.inneholderKode(PdlBadRequestException.KODE) -> throw PdlBadRequestException()
                    errors.inneholderKode(PdlUnauthenticatedException.KODE) -> throw PdlUnauthenticatedException()
                    else -> throw PdlClientException(errors)

                }
            }
            if (data != null){
                if (data.hentPerson.harAdressebeskyttelse()) {
                    throw PdlHarAdressebeskyttelseException()
                }
            }

            response
        }
    }

    suspend fun hentPerson(fnr: String, callId: String): GraphQLClientResponse<HentPerson.Result> {

        return runWithRetryAndMetrics("PDL", "HentPerson", retry) {
            val token = azureadClient.hentTokenScopetMotPDL()
            val query = HentPerson(
                variables = HentPerson.Variables(
                    fnr
                )
            )
            val response: KotlinxGraphQLResponse<HentPerson.Result> = httpClient.post() {
                url(baseUrl)
                setBody(query)
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
                header("Nav-Call-Id", callId)
                header("TEMA", "HJE")
            }.body()

            if (!response.errors.isNullOrEmpty()) {

                logger.error("PDL response errors: ${response.errors}")
                // TODO: utfør feil håndtering. Gjøres utenfor denne koden?
            }
            val data = response.data
            val errors = response.errors
            if (errors != null) {
                when (errors!=null){
                    errors.inneholderKode(PdlNotFoundException.KODE) -> throw PdlNotFoundException()
                    errors.inneholderKode(PdlBadRequestException.KODE) -> throw PdlBadRequestException()
                    errors.inneholderKode(PdlUnauthenticatedException.KODE) -> throw PdlUnauthenticatedException()
                    else -> throw PdlClientException(errors)

                }
            }
            if (data != null){
                if (data.hentPerson.harAdressebeskyttelse()) {
                    throw PdlHarAdressebeskyttelseException()
                }
            }

            response
        }
    }

    suspend fun medlemskapHentVergeEllerForelder(fnr: String, callId: String): KotlinxGraphQLResponse<MedlemskapHentVergeEllerForelder.Result> {

        return runWithRetryAndMetrics("PDL", "HentPerson", retry) {
            val token = azureadClient.hentTokenScopetMotPDL()
            val query = MedlemskapHentVergeEllerForelder(
                variables = MedlemskapHentVergeEllerForelder.Variables(
                    ident = fnr
                )
            )
            val response: KotlinxGraphQLResponse<MedlemskapHentVergeEllerForelder.Result> = httpClient.post() {
                url(baseUrl)
                setBody(query)
                header(HttpHeaders.Authorization, "Bearer ${token.token}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
                header("Nav-Call-Id", callId)
                header("TEMA", "HJE")
            }.body()

            if (!response.errors.isNullOrEmpty()) {

                logger.error("PDL response errors: ${response.errors}")
                // TODO: utfør feil håndtering. Gjøres utenfor denne koden?
            }
            val data = response.data
            val errors = response.errors
            if (errors != null) {
                when (errors!=null){
                    errors.inneholderKode(PdlNotFoundException.KODE) -> throw PdlNotFoundException()
                    errors.inneholderKode(PdlBadRequestException.KODE) -> throw PdlBadRequestException()
                    errors.inneholderKode(PdlUnauthenticatedException.KODE) -> throw PdlUnauthenticatedException()
                    else -> throw PdlClientException(errors)

                }
            }
            if (data != null){
                if (data.hentPerson.harAdressebeskyttelse()) {
                    throw PdlHarAdressebeskyttelseException()
                }
            }

            response
        }
    }

    private fun List<GraphQLClientError>.inneholderKode(kode: String) = this
        .map { it.extensions ?: emptyMap() }
        .map { it["code"] }
        .any { it == kode }


}
