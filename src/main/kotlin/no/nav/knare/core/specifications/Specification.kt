package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.Evaluation
import no.nav.knare.core.evaluations.Evaluation.Companion.and
import no.nav.knare.core.evaluations.Evaluation.Companion.evaluation
import no.nav.knare.core.evaluations.Evaluation.Companion.not
import no.nav.knare.core.evaluations.Evaluation.Companion.or

abstract class Specification<T> {
    open var description: () -> String = { "default description" }
    open var identity: () -> String = { "default identity" }
    abstract fun evaluate(t: T): Evaluation

    fun and(right: Specification<T>): Specification<T> {
        return AndSpecification(left = this, right = right)
    }

    fun or(right: Specification<T>): Specification<T> {
        return OrSpecification(left = this, right = right)
    }

    fun not(): Specification<T> {
        return NotSpecification(spec = this)
    }

    companion object {
        fun <T> rule(spec: Specification<T>, description: String, identity: String): Specification<T> {
            spec.description = fun(): String = description
            spec.identity = fun(): String = identity
            return spec
        }

        fun <T> not(spec: Specification<T>): Specification<T> {
            return NotSpecification(spec)
        }
    }
}

class AndSpecification<T>(
        val left: Specification<T>,
        val right: Specification<T>
) : Specification<T>() {
    override var description: () -> String = { "(${left.description} AND ${right.description})" }
    override var identity: () -> String = { "(${left.identity}) AND ${right.identity}" }
    override fun evaluate(t: T): Evaluation {
        return evaluation(description(), identity(), and(left.evaluate(t), right.evaluate(t)))
    }
}

class OrSpecification<T>(
        val left: Specification<T>,
        val right: Specification<T>
) : Specification<T>() {
    override var description: () -> String = { "(${left.description} OR ${right.description})" }
    override var identity: () -> String = { "(${left.identity}) OR ${right.identity}" }
    override fun evaluate(t: T): Evaluation {
        return evaluation(description(), identity(), or(left.evaluate(t), right.evaluate(t)))
    }
}

class NotSpecification<T>(
        val spec: Specification<T>
) : Specification<T>() {
    override var description: () -> String = { "(NOT ${spec.description})" }
    override var identity: () -> String = { "(NOT ${spec.identity})" }
    override fun evaluate(t: T): Evaluation {
        return evaluation(description(), identity(), not(spec.evaluate(t)))
    }
}
