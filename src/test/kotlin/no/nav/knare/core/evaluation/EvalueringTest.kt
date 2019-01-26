import no.nav.knare.core.evaluations.Evaluering
import no.nav.knare.core.evaluations.Evaluering.Companion.ja
import no.nav.knare.core.evaluations.Evaluering.Companion.nei
import no.nav.knare.core.evaluations.Operator
import no.nav.knare.core.evaluations.Resultat.JA
import no.nav.knare.core.evaluations.Resultat.NEI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EvalueringTest {

   @Test
   fun eller() {
      val left = ja("left begrunnelse")
      val right = nei("right begrunnelse")
      val expected = Evaluering(
         resultat = JA,
         begrunnelse = "(left begrunnelse ELLER right begrunnelse)",
         operator = Operator.ELLER,
         children = listOf(left, right))
      assertEquals(expected, left eller right)
   }

   @Test
   fun og() {
      val left = ja("left begrunnelse")
      val right = nei("right begrunnelse")
      val expected = Evaluering(resultat = NEI, begrunnelse = "(left begrunnelse OG right begrunnelse)", operator = Operator.OG, children = listOf(left, right))
      assertEquals(expected, left og right)
   }

   @Test
   fun ikke() {
      val orig = ja("orig begrunnelse")
      val expected = Evaluering(resultat = NEI, begrunnelse = "(IKKE orig begrunnelse)", operator = Operator.IKKE, children = listOf(orig))
      assertEquals(expected, orig.ikke())
   }
}
