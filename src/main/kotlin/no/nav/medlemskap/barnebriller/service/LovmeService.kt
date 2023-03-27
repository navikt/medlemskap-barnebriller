package no.nav.medlemskap.barnebriller.service;

import no.nav.medlemskap.barnebriller.clients.medloppslag.Brukerinput
import no.nav.medlemskap.barnebriller.clients.medloppslag.MedlOppslagRequest
import no.nav.medlemskap.barnebriller.clients.medloppslag.Periode
import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.rest.out.RestClients
import java.time.LocalDate

class LovmeService():ICanCallLovme {


        private val configuration = Configuration()

        private val restClients = RestClients(
                configuration = configuration
        )
        private val lovmeclient = restClients.lovme(configuration.register.medlemskapOppslagBaseUrl)
        override suspend fun slåOppMedlemskap(
                fnrVergeEllerForelder: String,
                bestillingsDato: LocalDate,
                correlationIdMedlemskap: String
        ): String {
               return lovmeclient.vurderMedlemskap(
                       medlOppslagRequest = MedlOppslagRequest(
                               fnr = fnrVergeEllerForelder,
                               førsteDagForYtelse = bestillingsDato.toString(),
                               periode = Periode(
                                       fom = bestillingsDato.toString(),
                                       tom = bestillingsDato.toString()
                               ),
                               brukerinput = Brukerinput(false)
                       ),
                       correlationIdMedlemskap
               )
        }

}