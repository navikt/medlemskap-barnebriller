package no.nav.medlemskap.barnebriller.jakson

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JacksonParserTest {

    @Test
    fun `mapping til response objekt for status Ja `(){
        val fileContent = this::class.java.classLoader.getResource("sampleVurdering.json").readText(Charsets.UTF_8)
        val parsed = MedlemskapVurdertParser().parseToMedlemskapResponse(fileContent)
        Assertions.assertNotNull(parsed)
    }

    @Test
    fun `mapping til response objekt for status UAVKLART `(){
        val fileContent = this::class.java.classLoader.getResource("regel_19_1_sample.json").readText(Charsets.UTF_8)
        val parsed = MedlemskapVurdertParser().parseToMedlemskapResponse(fileContent)
        Assertions.assertNotNull(parsed)
    }

}