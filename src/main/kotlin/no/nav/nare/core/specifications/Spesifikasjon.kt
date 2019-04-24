package no.nav.nare.core.specifications

import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.evaluations.Evaluering.Companion.evaluer
import java.util.Arrays.asList


data class Spesifikasjon<T>(
   val beskrivelse: String,
   val identitet: String = "",
   val children: List<Spesifikasjon<T>> = emptyList(),
   val implementasjon: T.() -> Evaluering) {

   val treeChildren: List<Spesifikasjon<T>>
      get() = (children + children.flatMap { if (it.identitet.isBlank()) it.treeChildren else listOf() })

   fun evaluer(t: T): Evaluering {
      return evaluer(
         beskrivelse = beskrivelse,
         identitet = identitet,
         eval = t.implementasjon())
   }

   infix fun og(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse OG ${other.beskrivelse}",
         children = addChildren(this, other),
         implementasjon = { evaluer(this) og other.evaluer(this) }
      )
   }

   infix fun eller(other: Spesifikasjon<T>): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "$beskrivelse ELLER ${other.beskrivelse}",
         children = addChildren(this, other),
         implementasjon = { evaluer(this) eller other.evaluer(this) }
      )
   }

   fun ikke(): Spesifikasjon<T> {
      return Spesifikasjon(
         beskrivelse = "IKKE $beskrivelse",
            children = (listOf(this)),
         implementasjon = { evaluer(this).ikke() }
      )
   }

   fun med(identitet: String, beskrivelse: String): Spesifikasjon<T> {
      return this.copy(beskrivelse = beskrivelse, identitet = identitet)

   }

   fun addChildren(s: Spesifikasjon<T>, o: Spesifikasjon<T>): List<Spesifikasjon<T>> {
      if (o.identitet.isBlank() && o.children.isNotEmpty()) return o.children + s
      if (s.identitet.isBlank() && s.children.isNotEmpty()) return s.children + o
      return listOf(s, o)
   }

}
fun <T> ikke(spec: Spesifikasjon<T>) = spec.ikke()
