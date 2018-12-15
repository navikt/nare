package no.nav.knare.core.specifications

import no.nav.knare.core.evaluations.Evaluation

open class Specification<T>(val evaluate: (T) -> Evaluation) {
    fun and(right: Specification<T>): Specification<T> {
        return AndSpecification(this, right)
    }

    fun or(right: Specification<T>): Specification<T> {
        return OrSpecification(this, right)
    }

    fun not(): Specification<T> {
        return NotSpecification(this)
    }
}

class AndSpecification<T>(left: Specification<T>, right: Specification<T>) : Specification<T>({ left.evaluate(it).and(right.evaluate(it)) })
class OrSpecification<T>(left: Specification<T>, right: Specification<T>) : Specification<T>({ left.evaluate(it).or(right.evaluate(it)) })
class NotSpecification<T>(spec: Specification<T>) : Specification<T>({ spec.evaluate(it).not() })
