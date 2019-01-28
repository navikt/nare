package no.nav.helse

import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.specifications.Spesifikasjon
import no.nav.nare.core.specifications.ikke

private val kanskje = Spesifikasjon<Søknad>("inngangsvilkår", "har ingen") {
   Evaluering.kanskje("vi vet ikke bedre")
}

val yrkesaktiv = Spesifikasjon<Søknad>("inngangsvilkår", "kapittel 8 p 2") {
   if (it.førsteSykdomsdag.minusDays(28) >= it.datoForAnsettelse) {
      Evaluering.ja("du har jobbet <antall> dager")
   } else {
      Evaluering.nei("kanskje")
   }
}

val opptjening = yrkesaktiv eller kanskje

val medlemskap_borINorgeNå = Spesifikasjon<Søknad>("medlemskap", "kapittel 2") {
   if (it.bostedLandISykdomsperiode == "Norge") {
      Evaluering.ja("Du bor i Norge")
   } else {
      Evaluering.nei("du må bo i Norge")
   }
}

val medlemskap = medlemskap_borINorgeNå.eller(kanskje)

val ytelser_harAndreYtelser = Spesifikasjon<Søknad>("inngangsvilkår", "") {
   if (it.ytelser.isEmpty()) {
      Evaluering.nei("har ingen andre ytelser")
   } else {
      Evaluering.ja("har andre ytelser")
   }
}

val ytelser = ikke(ytelser_harAndreYtelser).eller(kanskje)

val erSendtInnenTreMåneder = Spesifikasjon<Søknad>("er søknad sendt innen 3 måneder etter måneden for første dag i søknadsperioden", "") {
   val treMånederTilbake = it.søknadSendt.minusMonths(3).withDayOfMonth(1)

   if (treMånederTilbake <= it.førsteDagSøknadGjelderFor && it.førsteDagSøknadGjelderFor <= it.søknadSendt) {
      Evaluering.ja("søknaden er sendt tre måneder eller mindre etter første måned i søknadsperioden")
   } else {
      Evaluering.nei("søknad er sendt etter tre måneder etter første måned i søknadsperioden")
   }
}

val søknadSendtInnenforFrist = erSendtInnenTreMåneder eller kanskje

val inngangsvilkår = (yrkesaktiv og medlemskap og ytelser og søknadSendtInnenforFrist) eller kanskje
