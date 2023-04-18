package no.nav.medlemskap.barnebriller.config

import com.natpryce.konfig.*
import mu.KotlinLogging
import java.io.File
import java.io.FileNotFoundException
import java.util.*

private val logger = KotlinLogging.logger { }

private val defaultProperties = ConfigurationMap(
    mapOf(
        "AZURE_OPENID_CONFIG_TOKEN_ENDPOINT" to "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/token",
        "AZURE_APP_WELL_KNOWN_URL" to "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/v2.0/.well-known/openid-configuration",
        "AZURE_TENANT" to "966ac572-f5b7-4bbe-aa88-c76419c0f851",
        "AZURE_AUTHORITY_ENDPOINT" to "https://login.microsoftonline.com",
        "SERVICE_USER_USERNAME" to "srvmedlemskap",
        "SERVICE_USER_PASSWORD" to "Ks7NeOThPx9otSu",
        "NAIS_APP_NAME" to "",
        "NAIS_CLUSTER_NAME" to "localhost",
        "NAIS_APP_IMAGE" to "",
        "AZURE_APP_CLIENT_ID" to "35b73682-a318-443d-8557-2e241e3c5ab3",
        "AZURE_APP_CLIENT_SECRET" to "xE88Q~WyHPVy_ZofnwdQu1hD4F9N~vgRnTSWRdsG",
        "AZURE_SCOPE_MEDL_OPPSLAG" to "api://dev-gcp.medlemskap.medlemskap-oppslag/.default",
        "MEDL_OPPSLAG_API_KEY" to "",
        "MEDL_OPPSLAG_BASE_URL" to "https://medlemskap-oppslag.dev.intern.nav.no",
        "MEDL_OPPSLAG_CLIENT_ID" to "2719da58-489e-4185-9ee6-74b7e93763d2",
        "AZURE_APP_TENANT_ID" to "966ac572-f5b7-4bbe-aa88-c76419c0f851",
        "PDL_BASE_URL" to "https://pdl-api.dev-fss-pub.nais.io/graphql",
        "PDL_API_KEY" to "medlemskap-oppslag+765fec539fde4d2aa8ac33e3fbb9e4e5",
        "AZURE_SCOPE_PDL" to "api://dev-fss.pdl.pdl-api/.default"

    )
)

private val config = ConfigurationProperties.systemProperties() overriding
        EnvironmentVariables overriding
        defaultProperties

private fun String.configProperty(): String = config[Key(this, stringType)]

private fun String.readFile() =
    try {
        logger.info { "Leser fra azure-fil $this" }
        File(this).readText(Charsets.UTF_8)
    } catch (err: FileNotFoundException) {
        logger.warn { "Azure fil ikke funnet" }
        null
    }

private fun hentCommitSha(image: String): String {
    val parts = image.split(":")
    if (parts.size == 1) return image
    return parts[1].substring(0, 7)
}

data class Configuration(
    val register: Register = Register(),
    val sts: Sts = Sts(),
    val azureAd: AzureAd = AzureAd(),
    val cluster: String = "NAIS_CLUSTER_NAME".configProperty(),
    val commitSha: String = hentCommitSha("NAIS_APP_IMAGE".configProperty())
) {
    data class Register(
        val medlemskapOppslagBaseUrl: String = "MEDL_OPPSLAG_BASE_URL".configProperty(),
        val medlemskapOppslagClientID: String = "MEDL_OPPSLAG_CLIENT_ID".configProperty(),
        val scope_medlemskapOppslag:String = "AZURE_SCOPE_MEDL_OPPSLAG".configProperty(),
        val pdlBaseUrl: String = "PDL_BASE_URL".configProperty(),
        val pdlApiKey: String = "PDL_API_KEY".configProperty(),
        val scope_pdl:String = "AZURE_SCOPE_PDL".configProperty(),

    )

    data class Sts(
        val username: String = "SERVICE_USER_USERNAME".configProperty(),
        val password: String = "SERVICE_USER_PASSWORD".configProperty()
    )

    data class AzureAd(
        val clientId: String = "AZURE_APP_CLIENT_ID".configProperty(),
        val clientSecret: String = "AZURE_APP_CLIENT_SECRET".configProperty(),
        val jwtAudience: String = "AZURE_APP_CLIENT_ID".configProperty(),
        val tokenEndpoint: String = "AZURE_OPENID_CONFIG_TOKEN_ENDPOINT".configProperty().removeSuffix("/"),
        val azureAppWellKnownUrl: String = "AZURE_APP_WELL_KNOWN_URL".configProperty().removeSuffix("/"),
        val authorityEndpoint: String = "AZURE_AUTHORITY_ENDPOINT".configProperty().removeSuffix("/"),
        val tenant: String = "AZURE_APP_TENANT_ID".configProperty()
    )

    class PdlProperties {

        val baseUrl: String = ""
    }

    companion object {
        val locale = Locale("nb")
    }

}
