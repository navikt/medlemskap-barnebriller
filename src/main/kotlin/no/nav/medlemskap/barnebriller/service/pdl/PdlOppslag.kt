package no.nav.medlemskap.barnebriller.service.pdl

import com.fasterxml.jackson.databind.JsonNode

sealed interface PdlOppslag<T> {
    val data: T
    val rawData: JsonNode

    fun harAdressebeskyttelse(): Boolean =
        when (this) {
            is PdlOppslagPerson -> data.harAdressebeskyttelse()
            is PdlOppslagBarn -> data.harAdressebeskyttelse()
            is PdlOppslagVergeEllerForelder -> data.harAdressebeskyttelse()
        }
}

data class PdlOppslagPerson(
    override val data: Person?,
    override val rawData: JsonNode,
) : PdlOppslag<Person?>

data class PdlOppslagBarn(
    override val data: Barn?,
    override val rawData: JsonNode,
) : PdlOppslag<Barn?>

data class PdlOppslagVergeEllerForelder(
    override val data: VergeEllerForelder?,
    override val rawData: JsonNode,
) : PdlOppslag<VergeEllerForelder?>