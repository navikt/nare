import no.nav.knare.core.evaluations.*
import no.nav.knare.core.specifications.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class SpesifikasjonTest {

    private val left = Spesifikasjon<String>(
            "left beskrivelse",
            "left identitet",
            emptyList()) { Evaluering.ja("yepp") }

    private val right = Spesifikasjon<String>(
            "right beskrivelse",
            "right identitet",
            emptyList()) { Evaluering.nei("nope") }

    @Test
    fun eller() {
        val actual = left eller right
        assertEquals("left beskrivelse ELLER right beskrivelse", actual.beskrivelse)
        assertEquals("left identitet ELLER right identitet", actual.identitet)
        assertEquals(listOf(left, right), actual.children)
        assertEquals(Resultat.JA, actual.implementasjon.invoke("").resultat)
    }

    @Test
    fun og() {
        val actual = left og right
        assertEquals("left beskrivelse OG right beskrivelse", actual.beskrivelse)
        assertEquals("left identitet OG right identitet", actual.identitet)
        assertEquals(listOf(left, right), actual.children)
        assertEquals(Resultat.NEI, actual.implementasjon.invoke("").resultat)
    }

    @Test
    fun ikke() {
        val actual = left.ikke()
        assertEquals("IKKE left beskrivelse", actual.beskrivelse)
        assertEquals("IKKE left identitet", actual.identitet)
        assertEquals(listOf(left), actual.children)
        assertEquals(Resultat.NEI, actual.implementasjon.invoke("").resultat)
    }
}
