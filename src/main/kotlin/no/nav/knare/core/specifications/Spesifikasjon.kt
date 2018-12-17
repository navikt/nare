package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.Evaluering
import no.nav.knare.core.evaluations.Evaluering.Companion.og
import no.nav.knare.core.evaluations.Evaluering.Companion.evaluering
import no.nav.knare.core.evaluations.Evaluering.Companion.ikke
import no.nav.knare.core.evaluations.Evaluering.Companion.eller

abstract class Spesifikasjon<T> {
    open var beskrivelse: () -> String = { "default beskrivelse" }
    open var identitet: () -> String = { "default identitet" }
    abstract fun evaluer(t: T): Evaluering

    fun og(right: Spesifikasjon<T>): Spesifikasjon<T> {
        return OgSpesifikasjon(left = this, right = right)
    }

    fun eller(right: Spesifikasjon<T>): Spesifikasjon<T> {
        return EllerSpesifikasjon(left = this, right = right)
    }

    fun ikke(): Spesifikasjon<T> {
        return IkkeSpesifikasjon(spec = this)
    }

    companion object {
        fun <T> regel(spec: Spesifikasjon<T>, beskrivelse: String, identitet: String): Spesifikasjon<T> {
            spec.beskrivelse = fun(): String = beskrivelse
            spec.identitet = fun(): String = identitet
            return spec
        }

        fun <T> ikke(spec: Spesifikasjon<T>): Spesifikasjon<T> {
            return IkkeSpesifikasjon(spec)
        }
    }
}

class OgSpesifikasjon<T>(
        val left: Spesifikasjon<T>,
        val right: Spesifikasjon<T>
) : Spesifikasjon<T>() {
    override var beskrivelse: () -> String = { "(${left.beskrivelse()} OG ${right.beskrivelse()})" }
    override var identitet: () -> String = { "(${left.identitet()}) OG ${right.identitet()}" }
    override fun evaluer(t: T): Evaluering {
        return evaluering(beskrivelse(), identitet(), og(left.evaluer(t), right.evaluer(t)))
    }
}

class EllerSpesifikasjon<T>(
        val left: Spesifikasjon<T>,
        val right: Spesifikasjon<T>
) : Spesifikasjon<T>() {
    override var beskrivelse: () -> String = { "(${left.beskrivelse()} ELLER ${right.beskrivelse()})" }
    override var identitet: () -> String = { "(${left.identitet()}) ELLER ${right.identitet()}" }
    override fun evaluer(t: T): Evaluering {
        return evaluering(beskrivelse(), identitet(), eller(left.evaluer(t), right.evaluer(t)))
    }
}

class IkkeSpesifikasjon<T>(
        val spec: Spesifikasjon<T>
) : Spesifikasjon<T>() {
    override var beskrivelse: () -> String = { "(IKKE ${spec.beskrivelse()})" }
    override var identitet: () -> String = { "(IKKE ${spec.identitet()})" }
    override fun evaluer(t: T): Evaluering {
        return evaluering(beskrivelse(), identitet(), ikke(spec.evaluer(t)))
    }
}
