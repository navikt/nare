
import no.nav.nare.core.evaluations.Resultat
import no.nav.nare.core.evaluations.Resultat.Ja
import no.nav.nare.core.evaluations.Resultat.Kanskje
import no.nav.nare.core.evaluations.Resultat.Nei
import no.nav.nare.core.evaluations.eller
import no.nav.nare.core.evaluations.ikke
import no.nav.nare.core.evaluations.og
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ArgumentConversionException
import org.junit.jupiter.params.converter.ArgumentConverter
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

class ResultatTest {

   @ParameterizedTest
   @CsvSource(
      "Ja, Ja, Ja",
      "Ja, Nei, Ja",
      "Nei, Ja, Ja",
      "Nei, Nei, Nei",
      "Ja, Kanskje, Ja",
      "Nei, Kanskje, Kanskje",
      "Kanskje, Kanskje, Kanskje",
      "Kanskje, Ja, Ja",
      "Kanskje, Nei, Kanskje"
   )
   fun or(@ConvertWith(ResultatConverter::class) left: Resultat,
          @ConvertWith(ResultatConverter::class) right: Resultat,
          @ConvertWith(ResultatConverter::class) expected: Resultat) {
      assertEquals(expected, left eller right)
   }

   @ParameterizedTest
   @CsvSource(
      "Ja, Ja, Ja",
      "Ja, Nei, Nei",
      "Nei, Ja, Nei",
      "Nei, Nei, Nei",
      "Ja, Kanskje, Kanskje",
      "Nei, Kanskje, Nei",
      "Kanskje, Kanskje, Kanskje",
      "Kanskje, Ja, Kanskje",
      "Kanskje, Nei, Nei"
   )
   fun and(@ConvertWith(ResultatConverter::class) left: Resultat,
           @ConvertWith(ResultatConverter::class) right: Resultat,
           @ConvertWith(ResultatConverter::class) expected: Resultat) {
      assertEquals(expected, left og right)
   }

   @ParameterizedTest
   @CsvSource(
      "Ja, Nei",
      "Nei, Ja",
      "Kanskje, Kanskje"
   )
   fun not(@ConvertWith(ResultatConverter::class) given: Resultat,
           @ConvertWith(ResultatConverter::class) expected: Resultat) {
      assertEquals(expected, given.ikke())
   }
}

class ResultatConverter: ArgumentConverter {
   override fun convert(source: Any?, context: ParameterContext?) =
      when (source) {
         is String -> {
            when (source) {
                "Ja" -> Ja
                "Nei" -> Nei
                "Kanskje" -> Kanskje
                else -> throw ArgumentConversionException("source must be Ja, Nei or Kanskje")
            }
         }
         else -> throw ArgumentConversionException("source is null or not a string")
      }
}
