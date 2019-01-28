import no.nav.nare.core.evaluations.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*

class ResultatTest {

   @ParameterizedTest
   @CsvSource(
      "JA, JA, JA",
      "JA, NEI, JA",
      "NEI, JA, JA",
      "NEI, NEI, NEI",
      "JA, KANSKJE, JA",
      "NEI, KANSKJE, KANSKJE",
      "KANSKJE, KANSKJE, KANSKJE",
      "KANSKJE, JA, JA",
      "KANSKJE, NEI, KANSKJE"
   )
   fun or(left: Resultat, right: Resultat, evaluated: Resultat) {
      assertEquals(left eller right, evaluated)
   }

   @ParameterizedTest
   @CsvSource(
      "JA, JA, JA",
      "JA, NEI, NEI",
      "NEI, JA, NEI",
      "NEI, NEI, NEI",
      "JA, KANSKJE, KANSKJE",
      "NEI, KANSKJE, NEI",
      "KANSKJE, KANSKJE, KANSKJE",
      "KANSKJE, JA, KANSKJE",
      "KANSKJE, NEI, NEI"
   )
   fun and(left: Resultat, right: Resultat, evaluated: Resultat) {
      assertEquals(left og right, evaluated)
   }

   @ParameterizedTest
   @CsvSource(
      "JA, NEI",
      "NEI, JA",
      "KANSKJE, KANSKJE"
   )
   fun not(orig: Resultat, evaluated: Resultat) {
      assertEquals(evaluated, orig.ikke())
   }
}
