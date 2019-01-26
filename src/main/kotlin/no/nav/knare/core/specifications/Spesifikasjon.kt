package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.Evaluering
import no.nav.knare.core.evaluations.Evaluering.Companion.evaluer


data class Spesifikasjon<T>(
   val beskrivelse: String,
   val identitet: String,
   val children: List<Spesifikasjon<T>> = emptyList(),
   val implementasjon: (t: T) -> Evaluering) {

   fun evaluer(t: T): Evaluering {
      return evaluer(
         beskrivelse = beskrivelse,
         identitet = identitet,
         eval = implementasjon.invoke(t))
   }

   infix fun og(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse OG ${other.beskrivelse}",
         identitet = "$identitet OG ${other.identitet}",
         children = listOf(this, other),
         implementasjon = { t -> evaluer(t) og other.evaluer(t) }
      )
   }

   infix fun eller(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse ELLER ${other.beskrivelse}",
         identitet = "$identitet ELLER ${other.identitet}",
         children = listOf(this, other),
         implementasjon = { t -> evaluer(t) eller other.evaluer(t) }
      )
   }

   fun ikke(): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "IKKE $beskrivelse",
         identitet = "IKKE $identitet",
         children = listOf(this),
         implementasjon = { t -> evaluer(t).ikke() }
      )
   }

   fun med(identitet: String, beskrivelse: String): Spesifikasjon<T> {
      return this.copy(beskrivelse = beskrivelse, identitet = identitet)

   }
}
