package no.nav.medlemskap.barnebriller

import kotlinx.coroutines.runBlocking
import no.nav.medlemskap.barnebriller.service.pdl.PdlService


    fun main(args: Array<String>) {
        runBlocking { val pdlService = PdlService()
            val response = pdlService.medlemskapHentBarn("04481260863","1234")
            println(response.rawData.toPrettyString())
        }

    }
