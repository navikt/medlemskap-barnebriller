package no.nav.medlemskap.barnebriller.rest

import java.time.LocalDate

data class Request(
    val fnr: String,
    val bestillingsDato: LocalDate?,

)


