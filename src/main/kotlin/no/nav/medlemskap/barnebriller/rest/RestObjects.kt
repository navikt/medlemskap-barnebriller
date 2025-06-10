package no.nav.medlemskap.barnebriller.rest

import com.fasterxml.jackson.databind.JsonNode
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments.kv
import org.slf4j.MarkerFactory
import java.time.LocalDate

data class Request(
    val fnr: String,
    val bestillingsdato: LocalDate?,

    )

data class MedlemskapResultat(
    val resultat: Resultat,
    val saksgrunnlag: List<Saksgrunnlag>,
)

data class Saksgrunnlag(
    val kilde: SaksgrunnlagKilde,
    val saksgrunnlag: JsonNode,
)

enum class SaksgrunnlagKilde {
    MEDLEMSKAP_BARN,
    PDL,
    LOV_ME,
}
enum class Resultat {
    JA,
    NEI,
    UAVKLART,
}

fun finnRolleNavn(saksgrunnlag: Saksgrunnlag,pdlSaksgrunnlagBarn:Saksgrunnlag ): String {
    val rollenavn = saksgrunnlag.saksgrunnlag.get("rolle").textValue()
    if (!rollenavn.contains("FORELDER_ANSVAR-felles"))
        return rollenavn
    else
    return  rollenavn+findActualRole(saksgrunnlag.saksgrunnlag.get("fnr").textValue(),pdlSaksgrunnlagBarn)
}

private val logger = KotlinLogging.logger { }
private val teamLogs = MarkerFactory.getMarker("TEAM_LOGS")

fun MedlemskapResultat.logStatistics(callId: String, fnr: String) {
    val resultat = this.resultat
    val saksgrunlagListe = this.saksgrunnlag.filter { it.kilde == SaksgrunnlagKilde.LOV_ME }
    val pdlSaksgrunnlagBarn = this.saksgrunnlag.filter { it.kilde == SaksgrunnlagKilde.PDL }.filter { it.saksgrunnlag.get("fnr").textValue() == fnr }.first()
    if (saksgrunlagListe.isEmpty()){
        logger.info(
            teamLogs,
            "Medlemskap barn svarte ${resultat.name} for kall med id $callId",
            kv("fnr", fnr),
            kv("callId", callId),
            kv("resultat", resultat.name),
            kv("respons",objectMapper.writeValueAsString(this)),
        )
    }
    else if(saksgrunlagListe.size>1) {
        logger.info(
            teamLogs,
            "Medlemskap barn svarte ${resultat.name} for kall med id $callId",
            kv("fnr", fnr),
            kv("callId", callId),
            kv("resultat", resultat.name),
            kv("respons",objectMapper.writeValueAsString(this)),
            kv(

                finnRolleNavn(saksgrunlagListe[0],pdlSaksgrunnlagBarn),
                saksgrunlagListe[0].saksgrunnlag.getLovmeSvar()
            ),
            kv(
                finnRolleNavn(saksgrunlagListe[1],pdlSaksgrunnlagBarn),
                saksgrunlagListe[1].saksgrunnlag.getLovmeSvar()
            )

        )
    }
    else{
        logger.info(
            teamLogs,
            "Medlemskap barn svarte ${resultat.name} for kall med id $callId",
            kv("fnr", fnr),
            kv("callId", callId),
            kv("resultat", resultat.name),
            kv("respons",objectMapper.writeValueAsString(this)),
            kv(
                finnRolleNavn(saksgrunlagListe[0],pdlSaksgrunnlagBarn),
                saksgrunlagListe[0].saksgrunnlag.getLovmeSvar()
            ),
        )
    }


}

fun findActualRole(get: String?, pdlSaksgrunnlagBarn: Saksgrunnlag): String? {
   try{
   val array:JsonNode = pdlSaksgrunnlagBarn.saksgrunnlag.get("pdl").get("hentPerson").get("forelderBarnRelasjon")
    if (array.isArray){
        val fount = array.filter { it.get("relatertPersonsIdent").textValue() == get }.first()
       if (fount!= null){
           return fount.get("relatertPersonsRolle").textValue()
       }
    }
    return "UKJENT";}
   catch (t:Throwable){
       return ""
   }

}

fun JsonNode.getLovmeSvar(): String {
    return runCatching { this.get("lov_me").get("resultat").get("svar").textValue() }
        .getOrDefault("?")

}