package no.nav.medlemskap.barnebriller.service.pdl

import com.fasterxml.jackson.databind.JsonNode
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.config.retryRegistry
import no.nav.medlemskap.barnebriller.rest.objectMapper
import no.nav.medlemskap.barnebriller.rest.out.RestClients

interface ICanCallPDL {
    suspend fun medlemskapHentBarn(fnrBarn:String,callId:String): PdlOppslagBarn

    suspend fun medlemskapHentVergeEllerForelder(fnr: String, callId:String): PdlOppslagVergeEllerForelder
}

class PdlService():ICanCallPDL {
    private val stsRetry = retryRegistry.retry("STS")
    private val configuration = Configuration()

    private val restClients = RestClients(
        configuration = configuration
    )
    private val pdlClient = restClients.pdl(configuration.register.pdlBaseUrl)

    override suspend fun medlemskapHentBarn(fnrBarn: String,callId:String): PdlOppslagBarn {

        val response = pdlClient.medlemskapHentBarn(fnrBarn,callId)
        return PdlOppslagBarn(response.data!!.hentPerson, objectMapper.convertValue(response.data, JsonNode::class.java) )

    }


    override suspend fun medlemskapHentVergeEllerForelder(fnr: String, callId:String): PdlOppslagVergeEllerForelder {
        val response = pdlClient.medlemskapHentVergeEllerForelder(fnr,callId)
        return PdlOppslagVergeEllerForelder(response.data!!.hentPerson, objectMapper.convertValue(response.data, JsonNode::class.java))
    }
}

