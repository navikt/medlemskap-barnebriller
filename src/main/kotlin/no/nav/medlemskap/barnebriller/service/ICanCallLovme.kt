package no.nav.medlemskap.barnebriller.service

import java.time.LocalDate

interface ICanCallLovme {
    suspend fun slåOppMedlemskap(fnrVergeEllerForelder: String, bestillingsDato: LocalDate, correlationIdMedlemskap: String): String



}
