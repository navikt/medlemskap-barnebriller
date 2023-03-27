package no.nav.medlemskap.barnebriller

import kotlinx.coroutines.runBlocking
import no.nav.medlemskap.barnebriller.jakson.MedlemskapVurdertParser
import no.nav.medlemskap.barnebriller.service.LovmeService
import java.time.LocalDate


fun main(args: Array<String>) {
        runBlocking { val lovmeservice = LovmeService()
            //09418208349 fungerer ikke
            // 04481260863 gir svar
            val response = lovmeservice.slåOppMedlemskap("12467300103", LocalDate.now(),"1234")
            println(MedlemskapVurdertParser().parseToMedlemskapResponse(response))
        }

    }
