
import no.nav.nare.core.evaluations.Evaluering
import no.nav.nare.core.evaluations.Resultat
import no.nav.nare.core.specifications.Spesifikasjon
import no.nav.nare.core.specifications.ikke
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpesifikasjonTest {

   private val leaf1 = Spesifikasjon<String>(
      "leaf1 beskrivelse",
      "leaf1",
      emptyList()) { Evaluering.ja("yepp") }

   private val leaf2 = Spesifikasjon<String>(
      "leaf2 beskrivelse",
      "leaf2",
      emptyList()) { Evaluering.nei("nope") }


   private val leaf3 = Spesifikasjon<String>(
      "leaf3 beskrivelse",
      "leaf3",
      emptyList()) { Evaluering.nei("nope") }

   @Test
   fun eller() {
      val actual = leaf1 eller leaf2
      assertEquals("leaf1 beskrivelse ELLER leaf2 beskrivelse", actual.beskrivelse)
      assertEquals(listOf(leaf1, leaf2), actual.children)
      assertEquals(Resultat.Ja, actual.implementasjon.invoke("").resultat)
   }

   @Test
   fun og() {
      val actual = leaf1 og leaf2
      assertEquals("leaf1 beskrivelse OG leaf2 beskrivelse", actual.beskrivelse)
      assertEquals(listOf(leaf1, leaf2), actual.children)
      assertEquals(Resultat.Nei, actual.implementasjon.invoke("").resultat)
   }

   @Test
   fun ikke() {
      val actual = leaf1.ikke()
      assertEquals("!leaf1 beskrivelse", actual.beskrivelse)
      assertEquals(listOf(leaf1), actual.children)
      assertEquals(Resultat.Nei, actual.implementasjon.invoke("").resultat)
   }


   @Test
   fun `removes one intermediate node`(){
      val intermediate = (leaf2 og leaf1)
      val root = (intermediate og leaf3).med(identitet = "root", beskrivelse = "beskrivelse")
      assertEquals(3, root.children.size)
   }

   @Test
   fun `removes two intermediate nodes`(){
      val intermediate1 = (leaf2 og leaf1)
      val intermediate2 = (leaf2 og leaf1)
      val root = (intermediate1 og intermediate2 ).med(identitet = "root", beskrivelse = "beskrivelse")
      assertEquals(4, root.children.size)
   }

}
