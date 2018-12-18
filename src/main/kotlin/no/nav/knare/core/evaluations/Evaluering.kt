package no.nav.knare.core.evaluations

data class Evaluering(val resultat: Resultat,
                      val årsak: String,
                      var beskrivelse: String?,
                      var identitet: String,
                      var barn: Collection<Evaluering>) {

    companion object {
        fun ja(reason: String, description: String, identification: String) =
                Evaluering(Resultat.YES, reason, description, identification, emptyList())

        fun nei(reason: String, description: String, identification: String) =
                Evaluering(Resultat.NO, reason, description, identification, emptyList())

        fun kanskje(reason: String, description: String, identification: String) =
                Evaluering(Resultat.MAYBE, reason, description, identification, emptyList())

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
                barn = listOf(left, right)
        )

        fun eller(left: Evaluering, right: Evaluering) = Evaluering(
                resultat = left.resultat.or(right.resultat),
                årsak = "${left.årsak} ELLER ${right.årsak}",
                beskrivelse = "${left.beskrivelse ?: "no beskrivelse"} ELLER ${right.beskrivelse ?: "no beskrivelse"}",
                identitet = "${left.identitet} ELLER ${right.identitet}",
                barn = listOf(left, right)
        )

        fun ikke(that: Evaluering) = Evaluering(
                resultat = that.resultat.not(),
                årsak = "IKKE ${that.årsak}",
                beskrivelse = "IKKE ${that.beskrivelse}",
                identitet = "IKKE (${that.identitet})",
                barn = emptyList()
        )
    }
}
