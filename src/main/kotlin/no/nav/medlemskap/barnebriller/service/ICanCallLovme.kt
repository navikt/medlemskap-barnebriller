package no.nav.medlemskap.barnebriller.service

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

interface ICanCallLovme {
    fun slåOppMedlemskap(fnrVergeEllerForelder: String, bestillingsDato: LocalDate, correlationIdMedlemskap: String): JsonNode



}
