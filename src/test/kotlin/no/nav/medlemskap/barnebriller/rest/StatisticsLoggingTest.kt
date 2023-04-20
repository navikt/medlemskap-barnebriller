package no.nav.medlemskap.barnebriller.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import mu.KotlinLogging
import org.junit.jupiter.api.Test

class StatisticsLoggingTest {

    @Test
    fun test(){
        val logger = KotlinLogging.logger { }
        val fileContent =  this::class.java.classLoader.getResource("SampleResponse.json").readText(Charsets.UTF_8)
        val mapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .findAndRegisterModules()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        val response:MedlemskapResultat = mapper.readValue(fileContent)
        response.logStatistics(logger,"","25500995664")
        println()
    }
}