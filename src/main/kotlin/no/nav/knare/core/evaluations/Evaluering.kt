package no.nav.knare.core.evaluations

class Evaluering(
        val resultat: Resultat,
        val reason: String,
        val operatør: Operatør = Operatør.INGEN,
        var children: Collection<Evaluering>) {

    infix fun og(other: Evaluering) = Evaluering(
            resultat = resultat and other.resultat,
            reason = "$reason OG ${other.reason}",
            operatør = Operatør.OG,
            children = listOf(this, other)
    )

    infix fun eller(other: Evaluering) = Evaluering(
            resultat = resultat or other.resultat,
            reason = "$reason ELLER ${other.reason}",
            operatør = Operatør.ELLER,
            children = listOf(this, other)
    )

    fun ikke() = Evaluering(
            resultat = resultat.not(),
            reason = "IKKE $reason",
            operatør = Operatør.IKKE,
            children = listOf(this)
    )

    companion object {
        fun ja(reason: String) = Evaluering(Resultat.JA, reason, Operatør.INGEN, emptyList())

        fun nei(reason: String) = Evaluering(Resultat.NEI, reason, Operatør.INGEN, emptyList())
    }
}

enum class Operatør {
    OG, ELLER, IKKE, INGEN
}