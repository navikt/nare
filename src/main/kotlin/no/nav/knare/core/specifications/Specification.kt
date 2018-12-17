package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.Evaluation

abstract class Specification<T>(val evaluate: (T) -> Evaluation) {
    open var description: String = ""
    open var identity: String = ""

    fun and(right: Specification<T>, description: String = "", identity: String = ""): Specification<T> {
        return AndSpecification(left = this, right = right, description = description, identity = identity)
    }

    fun or(right: Specification<T>, description: String = "", identity: String = ""): Specification<T> {
        return OrSpecification(left = this, right = right, description =  description, identity = identity)
    }

    fun not(description: String = "", identity: String = ""): Specification<T> {
        return NotSpecification(spec = this, description = description, identity = identity)
    }

    companion object {
        fun <T> rule(description: String = "", identity: String = "", specification: Specification<T>): Specification<T> {
            specification.description = description
            specification.identity = identity
            return specification
        }
    }
}

class AndSpecification<T>(
        left: Specification<T>,
        right: Specification<T>,
        override var description: String = "(${left.description} AND ${right.description})",
        override var identity: String = "(${left.identity}) AND ${right.identity}"
) : Specification<T>({ left.evaluate(it).and(right.evaluate(it)) })

class OrSpecification<T>(
        left: Specification<T>,
        right: Specification<T>,
        override var description: String = "(${left.description} OR ${right.description})",
        override var identity: String = "(${left.identity}) OR ${right.identity}"
) : Specification<T>({ left.evaluate(it).or(right.evaluate(it)) })

class NotSpecification<T>(
        spec: Specification<T>,
        override var description: String = "(NOT ${spec.description})",
        override var identity: String = "(NOT ${spec.identity})"
) : Specification<T>({ spec.evaluate(it).not() })
