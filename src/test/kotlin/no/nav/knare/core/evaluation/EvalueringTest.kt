import no.nav.knare.core.evaluations.*
import no.nav.knare.core.evaluations.Evaluering.Companion.ja
import no.nav.knare.core.evaluations.Evaluering.Companion.nei
import no.nav.knare.core.evaluations.Resultat.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class EvalueringTest {

    @Test
    fun eller() {
        val left = ja("left reason")
        val right = nei("right reason")
        val expected = Evaluering(JA, "left reason ELLER right reason", Operatør.ELLER, listOf(left, right))
        assertEquals(expected, left eller right)
    }

    @Test
    fun og() {
        val left = ja("left reason")
        val right = nei("right reason")
        val expected = Evaluering(NEI, "left reason OG right reason", Operatør.OG, listOf(left, right))
        assertEquals(expected, left og right)
    }

    @Test
    fun ikke() {
        val orig = ja("orig reason")
        val expected = Evaluering(NEI, "IKKE orig reason", Operatør.IKKE, listOf(orig))
        assertEquals(expected, orig.ikke())
    }
}