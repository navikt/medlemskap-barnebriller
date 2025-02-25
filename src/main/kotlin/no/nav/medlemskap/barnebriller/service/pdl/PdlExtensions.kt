package no.nav.medlemskap.barnebriller.service.pdl


import no.nav.medlemskap.barnebriller.config.Configuration
import no.nav.medlemskap.barnebriller.pdl.generated.enums.AdressebeskyttelseGradering
import no.nav.medlemskap.barnebriller.pdl.generated.hentperson.Navn
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.util.*

typealias Person = no.nav.medlemskap.barnebriller.pdl.generated.hentperson.Person
typealias Barn = no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentbarn.Person
typealias VergeEllerForelder = no.nav.medlemskap.barnebriller.pdl.generated.medlemskaphentvergeellerforelder.Person

private fun <T> List<T>.firstOrDefault(default: T): T = firstOrNull() ?: default

private fun String.capitalizeWord(): String = this
    .split(" ")
    .joinToString(" ") { word ->
        word.lowercase(Configuration.locale)
            .replaceFirstChar { letter ->
                letter.titlecase(Configuration.locale)
            }
    }

fun Person?.harAdressebeskyttelse(): Boolean =
    when {
        this == null -> false
        else -> adressebeskyttelse.map { it.gradering }.erFortrolig()
    }

fun Barn?.harAdressebeskyttelse(): Boolean =
    when {
        this == null -> false
        else -> adressebeskyttelse.map { it.gradering }.erFortrolig()
    }

fun VergeEllerForelder?.harAdressebeskyttelse(): Boolean =
    when {
        this == null -> false
        else -> adressebeskyttelse.map { it.gradering }.erFortrolig()
    }

fun List<AdressebeskyttelseGradering>.erFortrolig() = any { gradering ->
    gradering in EnumSet
        .of(
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
            AdressebeskyttelseGradering.FORTROLIG,
        )
}

object HentPersonExtensions {
    fun Person.navn(): String {
        val navn = navn.firstOrDefault(Navn("", "", ""))
        return listOfNotNull(navn.fornavn, navn.mellomnavn, navn.etternavn)
            .filterNot { it.isBlank() }
            .joinToString(" ")
            .capitalizeWord()
    }
}