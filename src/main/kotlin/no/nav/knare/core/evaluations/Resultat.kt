package no.nav.knare.core.evaluations

enum class Resultat {
    JA {
        override infix fun and(other: Resultat): Resultat = other
        override infix fun or(other: Resultat): Resultat = JA
        override fun not(): Resultat = NEI
    },

    NEI {
        override infix fun and(other: Resultat): Resultat = NEI
        override infix fun or(other: Resultat): Resultat = other
        override fun not(): Resultat = JA
    },

    KANSKJE {
        override infix fun and(other: Resultat): Resultat = if (other == JA) KANSKJE else other
        override infix fun or(other: Resultat): Resultat = if (other == NEI) KANSKJE else other
        override fun not(): Resultat = KANSKJE
    };

    abstract infix fun and(other: Resultat): Resultat
    abstract infix fun or(other: Resultat): Resultat
    abstract fun not(): Resultat
}