package no.nav.helse

import no.nav.nare.core.evaluations.Resultat.JA
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class InngangsvilkårTest {

   @Test
   fun `du må oppfylle hele vilkåret`() {
      val førsteSykdomsdag = LocalDate.parse("2019-01-29")
      val datoForAnsettelse = LocalDate.parse("2019-01-01")
      val bostedLandISykdomsperiode = "Norge"
      val søknadSendt = LocalDate.parse("2019-04-30")
      val førsteDagSøknadGjelderFor = LocalDate.parse("2019-01-29")

      val soknad = Søknad(førsteSykdomsdag, datoForAnsettelse, bostedLandISykdomsperiode, emptyList(), søknadSendt, førsteDagSøknadGjelderFor)


      assertTrue(inngangsvilkår.evaluer(soknad).resultat == JA)
   }
}
