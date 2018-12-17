package no.nav.knare.core.evaluations

enum class Resultat {
    YES {
        override fun and(other: Resultat): Resultat = other
        override fun or(other: Resultat): Resultat = YES
        override fun not(): Resultat = NO
    },

    NO {
        override fun and(other: Resultat): Resultat = NO
        override fun or(other: Resultat): Resultat = other
        override fun not(): Resultat = YES
    },

    MAYBE {
        override fun and(other: Resultat): Resultat = if (other == YES) MAYBE else other
        override fun or(other: Resultat): Resultat = if (other == NO) MAYBE else other
        override fun not(): Resultat = MAYBE
    };

    abstract fun and(other: Resultat): Resultat
    abstract fun or(other: Resultat): Resultat
    abstract fun not(): Resultat
}