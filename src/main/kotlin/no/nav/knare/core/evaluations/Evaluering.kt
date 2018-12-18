package no.nav.knare.core.evaluations

data class Evaluering(val resultat: Resultat,
                      val årsak: String,
                      var beskrivelse: String?,
                      var identitet: String,
                      var operatør: Operatør = Operatør.INGEN,
                      var children: Collection<Evaluering>) {

    companion object {
        fun ja(reason: String, description: String, identification: String) =
                Evaluering(Resultat.JA, reason, description, identification, Operatør.INGEN, emptyList())

        fun nei(reason: String, description: String, identification: String) =
                Evaluering(Resultat.NEI, reason, description, identification, Operatør.INGEN, emptyList())

        fun kanskje(reason: String, description: String, identification: String) =
                Evaluering(Resultat.KANSKJE, reason, description, identification, Operatør.INGEN, emptyList())

        fun evaluering(beskrivelse: String = "", identitet: String = "", evaluering: Evaluering): Evaluering {
            evaluering.beskrivelse = beskrivelse
            evaluering.identitet = identitet
            return evaluering
        }

        fun og(left: Evaluering, right: Evaluering) = Evaluering(
                resultat = left.resultat.and(right.resultat),
                årsak = "${left.årsak} OG ${right.årsak}",
                beskrivelse = "${left.beskrivelse ?: "no beskrivelse"} OG ${right.beskrivelse ?: "no beskrivelse"}",
                identitet = "${left.identitet} OG ${right.identitet}",
                operatør = Operatør.OG,
                children = listOf(left, right)
        )

        fun eller(left: Evaluering, right: Evaluering) = Evaluering(
                resultat = left.resultat.or(right.resultat),
                årsak = "${left.årsak} ELLER ${right.årsak}",
                beskrivelse = "${left.beskrivelse ?: "no beskrivelse"} ELLER ${right.beskrivelse ?: "no beskrivelse"}",
                identitet = "${left.identitet} ELLER ${right.identitet}",
                operatør = Operatør.ELLER,
                children = listOf(left, right)
        )

        fun ikke(that: Evaluering) = Evaluering(
                resultat = that.resultat.not(),
                årsak = "IKKE ${that.årsak}",
                beskrivelse = "IKKE ${that.beskrivelse}",
                identitet = "IKKE (${that.identitet})",
                operatør = Operatør.IKKE,
                children = emptyList()
        )
    }
}

enum class Operatør {
    OG, ELLER, IKKE, INGEN
}