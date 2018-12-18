package no.nav.knare.core.evaluations

enum class Resultat {
    JA {
        override fun and(other: Resultat): Resultat = other
        override fun or(other: Resultat): Resultat = JA
        override fun not(): Resultat = NEI
    },

    NEI {
        override fun and(other: Resultat): Resultat = NEI
        override fun or(other: Resultat): Resultat = other
        override fun not(): Resultat = JA
    },

    KANSKJE {
        override fun and(other: Resultat): Resultat = if (other == JA) KANSKJE else other
        override fun or(other: Resultat): Resultat = if (other == NEI) KANSKJE else other
        override fun not(): Resultat = KANSKJE
    };

    abstract fun and(other: Resultat): Resultat
    abstract fun or(other: Resultat): Resultat
    abstract fun not(): Resultat
}