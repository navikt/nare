package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.Evaluation

abstract class Specification<T>(val evaluate: (T) -> Evaluation) {
    abstract val description: String
    abstract val identity: String

    fun and(right: Specification<T>, description: String = "", identity: String = ""): Specification<T> {
        return AndSpecification(left = this, right = right, description = description, identity = identity)
    }

    fun or(right: Specification<T>, description: String = "", identity: String = ""): Specification<T> {
        return OrSpecification(left = this, right = right, description =  description, identity = identity)
    }

    fun not(description: String = "", identity: String = ""): Specification<T> {
        return NotSpecification(spec = this, description = description, identity = identity)
    }
}

class AndSpecification<T>(
        left: Specification<T>,
        right: Specification<T>,
        override val description: String = "(${left.description} AND ${right.description})",
        override val identity: String = "(${left.identity}) AND ${right.identity}"
) : Specification<T>({ left.evaluate(it).and(right.evaluate(it)) })

class OrSpecification<T>(
        left: Specification<T>,
        right: Specification<T>,
        override val description: String = "(${left.description} OR ${right.description})",
        override val identity: String = "(${left.identity}) OR ${right.identity}"
) : Specification<T>({ left.evaluate(it).or(right.evaluate(it)) })

class NotSpecification<T>(
        spec: Specification<T>,
        override val description: String = "(NOT ${spec.description})",
        override val identity: String = "(NOT ${spec.identity})"
) : Specification<T>({ spec.evaluate(it).not() })
