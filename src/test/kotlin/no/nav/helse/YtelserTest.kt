package no.nav.helse

import no.nav.nare.core.evaluations.Resultat.JA
import no.nav.nare.core.evaluations.Resultat.KANSKJE
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class YtelserTest {

   @Test
   fun `du kan ha rett til sykepenger om du ikke har andre ytelser`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-29")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)


      assertTrue(ytelser.evaluer(soknad).resultat == JA)
   }

   @Test
   fun `du kan ha rett til sykepenger om du har andre ytelser`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-28")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Sverige"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, listOf("Dagpenger"), søknadSendt, førsteDagSøknadGjelderFor)


      assertTrue(ytelser.evaluer(soknad).resultat == KANSKJE)
   }
}
