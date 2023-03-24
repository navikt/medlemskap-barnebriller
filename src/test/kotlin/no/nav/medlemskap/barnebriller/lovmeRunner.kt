package no.nav.medlemskap.barnebriller

import kotlinx.coroutines.runBlocking
import no.nav.medlemskap.barnebriller.jakson.MedlemskapVurdertParser
import no.nav.medlemskap.barnebriller.service.LovmeService
import java.time.LocalDate


fun main(args: Array<String>) {
        runBlocking { val lovmeservice = LovmeService()
            val response = lovmeservice.slåOppMedlemskap("04481260863", LocalDate.now(),"1234")
            println(MedlemskapVurdertParser().parseToMedlemskapResponse(response))
        }

    }
