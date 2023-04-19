package no.nav.medlemskap.barnebriller.rest

import com.fasterxml.jackson.databind.JsonNode
import mu.KLogger
import net.logstash.logback.argument.StructuredArguments.kv
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

fun MedlemskapResultat.logStatistics(logger: KLogger, callId: String, fnr: String) {
    val resultat = this.resultat
    val SaksgrunlagListe = this.saksgrunnlag.filter { it.kilde == SaksgrunnlagKilde.LOV_ME }

    logger.info("Medlemskap barn svarte ${resultat.name} for kall med id $callId",
        kv("fnr", fnr),
        kv("callId", callId),
        kv("resultat", resultat.name),
        SaksgrunlagListe.forEach {
            kv(it.saksgrunnlag.get("rolle").textValue(),it.saksgrunnlag.getLovmeSvar())
        }
        )
}
fun JsonNode.getLovmeSvar(): String {
    return runCatching { this.get("lov_me").get("resultat").get("svar").textValue() }
        .getOrDefault("?")

}
