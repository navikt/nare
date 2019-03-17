package no.nav.nare.core.evaluations

import no.nav.nare.core.evaluations.Resultat.Ja
import no.nav.nare.core.evaluations.Resultat.Kanskje
import no.nav.nare.core.evaluations.Resultat.Nei

sealed class Resultat {
   object Ja: Resultat()
   object Nei: Resultat()
   object Kanskje: Resultat()
}

infix fun Resultat.og(other: Resultat) = when (this) {
   is Ja -> other
   is Nei -> Nei
   is Kanskje -> when (other) {
      is Ja -> Kanskje
      else -> other
   }
}

infix fun Resultat.eller(other: Resultat) = when (this) {
   is Ja -> Ja
   is Nei -> other
   is Kanskje -> when (other) {
      is Nei -> Kanskje
      else -> other
   }
}

fun Resultat.ikke() = when (this) {
   is Ja -> Nei
   is Nei -> Ja
   is Kanskje -> Kanskje
}
