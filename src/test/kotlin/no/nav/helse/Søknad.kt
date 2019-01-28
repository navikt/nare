package no.nav.helse

import java.time.LocalDate

data class Søknad(val førsteSykdomsdag: LocalDate, val datoForAnsettelse: LocalDate, val bostedLandISykdomsperiode: String,
                  val ytelser: List<String>, val søknadSendt: LocalDate,
                  val førsteDagSøknadGjelderFor: LocalDate)
