package no.nav.nare.core.evaluations

data class Evaluering(
   val resultat: Resultat,
   val begrunnelse: String,
   val beskrivelse: String = "",
   val identifikator: String = "",
   val operator: Operator = Operator.INGEN,
   var children: List<Evaluering> = emptyList()) {

   infix fun og(other: Evaluering) = Evaluering(
      resultat = resultat og other.resultat,
      begrunnelse = "($begrunnelse OG ${other.begrunnelse})",
      operator = Operator.OG,
      children = this.specOrChildren() + other.specOrChildren()
      )

   infix fun eller(other: Evaluering) = Evaluering(
      resultat = resultat eller other.resultat,
      begrunnelse = "($begrunnelse ELLER ${other.begrunnelse})",
      operator = Operator.ELLER,
      children = this.specOrChildren() + other.specOrChildren()
      )

   fun ikke() = Evaluering(
      resultat = resultat.ikke(),
      begrunnelse = "(IKKE $begrunnelse)",
      operator = Operator.IKKE,
      children = listOf(this)
   )

   private fun specOrChildren(): List<Evaluering> =
      if (identifikator.isBlank() && children.isNotEmpty()) children else listOf(this)

   companion object {
      fun ja(begrunnelse: String) = Evaluering(Resultat.Ja, begrunnelse)

      fun nei(begrunnelse: String) = Evaluering(Resultat.Nei, begrunnelse)

      fun kanskje(begrunnelse: String) = Evaluering(Resultat.Kanskje, begrunnelse)

      fun evaluer(identifikator: String, beskrivelse: String, eval: Evaluering) = eval.copy(identifikator = identifikator, beskrivelse = beskrivelse)
   }

}

enum class Operator {
   OG, ELLER, IKKE, INGEN
}


