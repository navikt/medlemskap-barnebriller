package no.nav.medlemskap.barnebriller.service

import com.fasterxml.jackson.databind.JsonNode
import kotlinx.coroutines.runBlocking
import no.nav.medlemskap.barnebriller.pdl.generated.enums.AdressebeskyttelseGradering
import no.nav.medlemskap.barnebriller.pdl.generated.enums.FullmaktsRolle
import no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentbarn.Adressebeskyttelse
import no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentbarn.Bostedsadresse
import no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentbarn.Fullmakt
import no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentbarn.Vegadresse
import no.nav.medlemskap.barnebriller.rest.Request
import no.nav.medlemskap.barnebriller.rest.objectMapper
import no.nav.medlemskap.barnebriller.service.pdl.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class BarneBrillerServiceTest {
    @Test
    fun `adressebesjyttelse på barn skal føre til medlemskap uten kall mor LOVME`(){
        val pdlmock = MockPdlService()
        val lovmemock = MockMedlemskap()
        pdlmock.barn = Barn(bostedsadresse = emptyList(),
            deltBosted = emptyList(),
            adressebeskyttelse = listOf(Adressebeskyttelse(AdressebeskyttelseGradering.FORTROLIG)),
            foedsel = emptyList(),
            foreldreansvar = emptyList(),
            forelderBarnRelasjon = emptyList(),
            fullmakt = emptyList(),
            vergemaalEllerFremtidsfullmakt = emptyList()
        )
        val service:BarneBrilleRequestService = BarneBrilleRequestService(pdlmock,lovmemock)
        runBlocking {
            val response = service.handle(Request("123456789801", LocalDate.now()),"1234")
            Assertions.assertEquals(true,response.medlemskapBevist,"medlemskap skal være gitt når adressebeskyttelse finnes")
            Assertions.assertEquals(true,response.saksgrunnlag.isEmpty(),"saksgrunnlag skal være tomt når adressebeskyttelse finnes")
        }
    }
    @Test
    fun `barn uten Norsk folkeregistrert adresse skal returnere ikke medlem`(){
        val mock = MockPdlService()
        val lovmemock = MockMedlemskap()
        mock.barn = Barn(bostedsadresse = emptyList(),
            deltBosted = emptyList(),
            adressebeskyttelse = emptyList(),
            foedsel = emptyList(),
            foreldreansvar = emptyList(),
            forelderBarnRelasjon = emptyList(),
            fullmakt = emptyList(),
            vergemaalEllerFremtidsfullmakt = emptyList()
        )
        val service:BarneBrilleRequestService = BarneBrilleRequestService(mock,lovmemock)
        runBlocking {
            val response = service.handle(Request("123456789801", LocalDate.now()),"1234")
            Assertions.assertFalse(response.medlemskapBevist,"medlemskap skal ikke gis til barn med utland adresser")
            Assertions.assertEquals(0,lovmemock.antallKall,"Lovme skal ikke kalles for barn med utland adresse")
        }
    }
    @Test
    fun `barn med Norsk folkeregistrert adresse skal slå opp forelder`(){
        val pdlMock = MockPdlService()
        val lovmemock = MockMedlemskap()
        val dateTime = LocalDateTime.of(2021,1,1,12,0,0)
        val bostedsadresse_barn = Bostedsadresse(
           vegadresse = Vegadresse(1223,"14a"),
            gyldigFraOgMed =dateTime,
            gyldigTilOgMed = LocalDateTime.MAX

        )
        val bostedsadresse_verge = no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Bostedsadresse(
            vegadresse = no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Vegadresse(1223,"14a"),
            gyldigFraOgMed =dateTime,
            gyldigTilOgMed = LocalDateTime.MAX

        )
        val fullmakt = Fullmakt(
            "1234",
            FullmaktsRolle.FULLMEKTIG,
            omraader = emptyList(),
            gyldigFraOgMed= LocalDate.of(2020,1,1),
            gyldigTilOgMed = LocalDate.MAX,

        )
        pdlMock.barn = Barn(bostedsadresse = listOf<Bostedsadresse>(bostedsadresse_barn),
            deltBosted = emptyList(),
            adressebeskyttelse = emptyList(),
            foedsel = emptyList(),
            foreldreansvar = emptyList(),
            forelderBarnRelasjon = emptyList(),
            fullmakt = listOf(fullmakt),
            vergemaalEllerFremtidsfullmakt = emptyList()
        )
        pdlMock.vergeEllerForelder = VergeEllerForelder(
            bostedsadresse = listOf<no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Bostedsadresse>(bostedsadresse_verge),
            adressebeskyttelse = emptyList()
        )
        lovmemock.response = reafFromResourceFile("sampleVurdering.json")
        val service = BarneBrilleRequestService(pdlMock,lovmemock)
        runBlocking {
            val response = service.handle(Request("123456789801", LocalDate.now()),"1234")
            Assertions.assertEquals(1,lovmemock.antallKall,"Lovme skal ikke kalles for barn med utland adresse")
            Assertions.assertTrue(response.medlemskapBevist)
        }
    }
    @Test
    fun `barn med Norsk folkeregistrert adresse og forelder ikke medlem skal ikke bli medlem`(){
        val pdlMock = MockPdlService()
        val lovmemock = MockMedlemskap()
        val dateTime = LocalDateTime.of(2021,1,1,12,0,0)
        val bostedsadresse_barn = Bostedsadresse(
            vegadresse = Vegadresse(1223,"14a"),
            gyldigFraOgMed =dateTime,
            gyldigTilOgMed = LocalDateTime.MAX

        )
        val bostedsadresse_verge = no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Bostedsadresse(
            vegadresse = no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Vegadresse(1223,"14a"),
            gyldigFraOgMed =dateTime,
            gyldigTilOgMed = LocalDateTime.MAX

        )
        val fullmakt = Fullmakt(
            "1234",
            FullmaktsRolle.FULLMEKTIG,
            omraader = emptyList(),
            gyldigFraOgMed= LocalDate.of(2020,1,1),
            gyldigTilOgMed = LocalDate.MAX,

            )
        pdlMock.barn = Barn(bostedsadresse = listOf<Bostedsadresse>(bostedsadresse_barn),
            deltBosted = emptyList(),
            adressebeskyttelse = emptyList(),
            foedsel = emptyList(),
            foreldreansvar = emptyList(),
            forelderBarnRelasjon = emptyList(),
            fullmakt = listOf(fullmakt),
            vergemaalEllerFremtidsfullmakt = emptyList()
        )
        pdlMock.vergeEllerForelder = VergeEllerForelder(
            bostedsadresse = listOf<no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Bostedsadresse>(bostedsadresse_verge),
            adressebeskyttelse = emptyList()
        )
        lovmemock.response = reafFromResourceFile("regel_19_1_sample.json")
        val service = BarneBrilleRequestService(pdlMock,lovmemock)
        runBlocking {
            val response = service.handle(Request("123456789801", LocalDate.now()),"1234")
            Assertions.assertEquals(1,lovmemock.antallKall,"Lovme skal ikke kalles for barn med utland adresse")
            Assertions.assertFalse(response.medlemskapBevist)
            Assertions.assertTrue(response.uavklartMedlemskap)

        }
    }

    private fun reafFromResourceFile(file: String): String? {
        return this::class.java.classLoader.getResource(file).readText(Charsets.UTF_8)
    }
}


class MockPdlService():ICanCallPDL{
    var kallmotmedlemskapHentBarn =0;
    var hentPerson =0;
    var medlemskapHentVergeEllerForelder =0;
    var barn:Barn? = null
    var person:Person? = null
    var vergeEllerForelder:VergeEllerForelder? = null
    override suspend fun medlemskapHentBarn(fnrBarn: String, callId: String): PdlOppslagBarn {
        kallmotmedlemskapHentBarn++
        return PdlOppslagBarn(barn, objectMapper.convertValue(barn, JsonNode::class.java))
    }


    override suspend fun medlemskapHentVergeEllerForelder(fnr: String, callId: String): PdlOppslagVergeEllerForelder {
        medlemskapHentVergeEllerForelder++
        return PdlOppslagVergeEllerForelder(vergeEllerForelder, objectMapper.convertValue(vergeEllerForelder, JsonNode::class.java))
    }

}

class MockMedlemskap():ICanCallLovme {
     var antallKall = 0;
     var fnr:String? = null
     var bestillingsDato:LocalDate? = null
     var correlationIdMedlemskap:String? = null
     var response:String? = null


    override suspend fun slåOppMedlemskap(
        fnrVergeEllerForelder: String,
        bestillingsDato: LocalDate,
        correlationIdMedlemskap: String
    ): String {
        antallKall++
        fnr = fnrVergeEllerForelder
        this.bestillingsDato=bestillingsDato
        this.correlationIdMedlemskap=correlationIdMedlemskap
        return response!!;

    }
}