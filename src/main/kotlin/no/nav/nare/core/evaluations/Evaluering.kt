package no.nav.nare.core.evaluations

data class Evaluering(
   val resultat: Resultat,
   val begrunnelse: String,
   val beskrivelse: String ="",
   val identifikator: String ="",
   val operator: Operator = Operator.INGEN,
   var children: List<Evaluering> = emptyList()) {

   infix fun og(other: Evaluering) = Evaluering(
      resultat = resultat og other.resultat,
      begrunnelse = "($begrunnelse OG ${other.begrunnelse})",
      operator = Operator.OG,
      children = addChildren(other)
   )

   infix fun eller(other: Evaluering) = Evaluering(
      resultat = resultat eller other.resultat,
      begrunnelse = "($begrunnelse ELLER ${other.begrunnelse})",
      operator = Operator.ELLER,
      children = addChildren(other)
   )

   fun ikke() = Evaluering(
      resultat = resultat.ikke(),
      begrunnelse = "(IKKE $begrunnelse)",
      operator = Operator.IKKE,
      children = listOf(this)
   )

   fun addChildren(other: Evaluering): List<Evaluering> {
      if (this.identifikator.isBlank() && this.children.isNotEmpty()) return this.children + other
      if (other.identifikator.isBlank() && other.children.isNotEmpty()) return other.children + this
      return listOf(this, other)
   }

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


