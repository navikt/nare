package no.nav.knare.core.evaluations

enum class Result {
    YES {
        override fun and(other: Result): Result = other
        override fun or(other: Result): Result = YES
        override fun not(): Result = NO
    },

    NO {
        override fun and(other: Result): Result = NO
        override fun or(other: Result): Result = other
        override fun not(): Result = YES
    },

    MAYBE {
        override fun and(other: Result): Result = if (other == YES) MAYBE else other
        override fun or(other: Result): Result = if (other == NO) MAYBE else other
        override fun not(): Result = MAYBE
    };

    abstract fun and(other: Result): Result
    abstract fun or(other: Result): Result
    abstract fun not(): Result
}