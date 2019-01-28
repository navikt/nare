package no.nav.helse

import no.nav.nare.core.evaluations.Resultat.JA
import no.nav.nare.core.evaluations.Resultat.KANSKJE
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SøknadsfristTest {

   @Test
   fun `søknad må være sendt innen 3 måneder etter første måned i søknadsperioden`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-29")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)

      assertTrue(søknadSendtInnenforFrist.evaluer(soknad).resultat == JA)
   }

   @Test
   fun `søknad kan ikke være sendt mer enn 3 måneder etter første måned i søknadsperioden`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-29")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-05-01")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)

      assertTrue(søknadSendtInnenforFrist.evaluer(soknad).resultat == KANSKJE)
   }

   @Test
   fun `søknad kan ikke være sendt før søknadsperioden`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-29")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2018-12-31")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)

      assertTrue(søknadSendtInnenforFrist.evaluer(soknad).resultat == KANSKJE)
   }
}
