package no.nav.nare.core.specifications

import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.evaluations.Evaluering.Companion.evaluer


data class Spesifikasjon<T>(
   val beskrivelse: String,
   val identifikator: String = "",
   val children: List<Spesifikasjon<T>> = emptyList(),
   val implementasjon: T.() -> Evaluering) {

   val treeChildren: List<Spesifikasjon<T>>
      get() = (children + children.flatMap { if (it.identifikator.isBlank()) it.treeChildren else listOf() })

   fun evaluer(t: T): Evaluering {
      return evaluer(
         beskrivelse = beskrivelse,
         identifikator = identifikator,
         eval = t.implementasjon())
   }

   infix fun og(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse OG ${other.beskrivelse}",
         children = this.specOrChildren() + other.specOrChildren(),
         implementasjon = { evaluer(this) og other.evaluer(this) }
      )
   }

   infix fun eller(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse ELLER ${other.beskrivelse}",
         children = this.specOrChildren() + other.specOrChildren(),
         implementasjon = { evaluer(this) eller other.evaluer(this) }
      )
   }

   fun ikke(): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "!$beskrivelse",
         identifikator = "!$identifikator",
         children = listOf(this),
         implementasjon = { evaluer(this).ikke() }
      )
   }

   fun med(identitet: String, beskrivelse: String): Spesifikasjon<T> {
      return this.copy(beskrivelse = beskrivelse, identifikator = identitet)
   }

   private fun specOrChildren(): List<Spesifikasjon<T>> =
      if (identifikator.isBlank() && children.isNotEmpty()) children else listOf(this)

}

fun <T> ikke(spec: Spesifikasjon<T>) = spec.ikke()
