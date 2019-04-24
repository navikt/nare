package no.nav.nare.core.specifications

import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.evaluations.Evaluering.Companion.evaluer


data class Spesifikasjon<T>(
   val beskrivelse: String,
   val identitet: String,
   val children: List<Spesifikasjon<T>> = emptyList(),
   val implementasjon: T.() -> Evaluering) {

   fun evaluer(t: T): Evaluering {
      return evaluer(
         beskrivelse = beskrivelse,
         identitet = identitet,
         eval = t.implementasjon())
   }

   infix fun og(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse OG ${other.beskrivelse}",
         identitet = "$identitet OG ${other.identitet}",
         children = listOf(this, other),
         implementasjon = { evaluer(this) og other.evaluer(this) }
      )
   }

   infix fun eller(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse ELLER ${other.beskrivelse}",
         identitet = "$identitet ELLER ${other.identitet}",
         children = listOf(this, other),
         implementasjon = { evaluer(this) eller other.evaluer(this) }
      )
   }

   fun ikke(): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "IKKE $beskrivelse",
         identitet = "IKKE $identitet",
         children = listOf(this),
         implementasjon = { evaluer(this).ikke() }
      )
   }

   fun med(identitet: String, beskrivelse: String): Spesifikasjon<T> {
      return this.copy(beskrivelse = beskrivelse, identitet = identitet)

   }
}

fun <T> ikke(spec: Spesifikasjon<T>) = spec.ikke()
