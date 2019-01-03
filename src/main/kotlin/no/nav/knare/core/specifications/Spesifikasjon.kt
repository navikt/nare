package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.*

data class Spesifikasjon (
        val beskrivelse: String,
        val identitet: String,
        val children: List<Spesifikasjon> = emptyList(),
        val implementasjon: () -> Evaluering) {

    fun evaluer() = implementasjon.invoke()

    infix fun og(other: Spesifikasjon): Spesifikasjon {
        return Spesifikasjon(
                beskrivelse = "$beskrivelse OG ${other.beskrivelse}",
                identitet = "$identitet OG ${other.identitet}",
                children = listOf(this, other),
                implementasjon = { evaluer() og other.evaluer() }
        )
    }

    infix fun eller(other: Spesifikasjon): Spesifikasjon {
        return Spesifikasjon(
                beskrivelse = "$beskrivelse ELLER ${other.beskrivelse}",
                identitet = "$identitet ELLER ${other.identitet}",
                children = listOf(this, other),
                implementasjon = { evaluer() eller other.evaluer() }
        )
    }

    fun ikke(): Spesifikasjon {
        return Spesifikasjon(
                beskrivelse = "IKKE $beskrivelse",
                identitet = "IKKE $identitet",
                children = listOf(this),
                implementasjon = { evaluer().ikke() }
        )
    }

}
