package no.nav.knare.core.evaluations

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
            children = listOf(this, other)
    )

    infix fun eller(other: Evaluering) = Evaluering(
            resultat = resultat eller other.resultat,
            begrunnelse = "($begrunnelse ELLER ${other.begrunnelse})",
            operator = Operator.ELLER,
            children = listOf(this, other)
    )

    fun ikke() = Evaluering(
            resultat = resultat.ikke(),
            begrunnelse = "(IKKE $begrunnelse)",
            operator = Operator.IKKE,
            children = listOf(this)
    )

    companion object {
        fun ja(begrunnelse: String) = Evaluering(Resultat.JA, begrunnelse)

        fun nei(begrunnelse: String) = Evaluering(Resultat.NEI, begrunnelse)

        fun evaluer(identitet: String, beskrivelse: String, eval: Evaluering) = eval.copy(beskrivelse = beskrivelse, identifikator = identitet)
    }

}

enum class Operator {
    OG, ELLER, IKKE, INGEN
}


