package no.nav.helse

import no.nav.nare.core.evaluations.Resultat.JA
import no.nav.nare.core.evaluations.Resultat.KANSKJE
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class OpptjeningTest {

   @Test
   fun `du må ha jobbet i 28 dager`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-29")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)


      assertTrue(opptjening.evaluer(soknad).resultat == JA)
   }

   @Test
   fun `du kan ikke jobbe mindre enn 28 dager`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-28")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)


      assertTrue(opptjening.evaluer(soknad).resultat == KANSKJE)
   }

   @Test
   fun `du kan jobbe mer enn 28 dager`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-30")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)


      assertTrue(opptjening.evaluer(soknad).resultat == JA)
   }
}
