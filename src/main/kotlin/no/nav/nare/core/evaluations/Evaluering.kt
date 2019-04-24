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
      children = addChildren(this, other)
   )

   infix fun eller(other: Evaluering) = Evaluering(
      resultat = resultat eller other.resultat,
      begrunnelse = "($begrunnelse ELLER ${other.begrunnelse})",
      operator = Operator.ELLER,
      children = addChildren(this, other)
   )

   fun ikke() = Evaluering(
      resultat = resultat.ikke(),
      begrunnelse = "(IKKE $begrunnelse)",
      operator = Operator.IKKE,
      children = listOf(this)
   )

   fun addChildren(s: Evaluering, o: Evaluering): List<Evaluering> {
      if (o.identifikator.isBlank() && o.children.isNotEmpty()) return o.children + s
      if (s.identifikator.isBlank() && s.children.isNotEmpty()) return s.children + o
      return listOf(s, o)
   }

   companion object {
      fun ja(begrunnelse: String) = Evaluering(Resultat.Ja, begrunnelse)

      fun nei(begrunnelse: String) = Evaluering(Resultat.Nei, begrunnelse)

      fun kanskje(begrunnelse: String) = Evaluering(Resultat.Kanskje, begrunnelse)

      fun evaluer(identitet: String, beskrivelse: String, eval: Evaluering) = eval.copy(beskrivelse = beskrivelse, identifikator = identitet)
   }

}

enum class Operator {
   OG, ELLER, IKKE, INGEN
}


