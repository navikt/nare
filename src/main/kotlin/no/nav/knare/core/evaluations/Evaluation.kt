package no.nav.knare.core.evaluations

data class Evaluation(val result: Result,
                      val reason: String,
                      var description: String?,
                      var identification: String,
                      var children: Collection<Evaluation>) {

    companion object {
        fun ja(reason: String, description: String, identification: String) =
                Evaluation(Result.YES, reason, description, identification, emptyList())

        fun nei(reason: String, description: String, identification: String) =
                Evaluation(Result.NO, reason, description, identification, emptyList())

        fun kanskje(reason: String, description: String, identification: String) =
                Evaluation(Result.MAYBE, reason, description, identification, emptyList())

        fun evaluation(description: String = "", identity: String = "", evaluation: Evaluation): Evaluation {
            evaluation.description = description
            evaluation.identification = identity
            return evaluation
        }

        fun and(left: Evaluation, right: Evaluation) = Evaluation(
                result = left.result.and(right.result),
                reason = "${left.reason} AND ${right.reason}",
                description = "${left.description ?: "no description"} AND ${right.description ?: "no description"}",
                identification = "${left.identification} AND ${right.identification}",
                children = listOf(left, right)
        )

        fun or(left: Evaluation, right: Evaluation) = Evaluation(
                result = left.result.or(right.result),
                reason = "${left.reason} OR ${right.reason}",
                description = "${left.description ?: "no description"} OR ${right.description ?: "no description"}",
                identification = "${left.identification} OR ${right.identification}",
                children = listOf(left, right)
        )

        fun not(that: Evaluation) = Evaluation(
                result = that.result.not(),
                reason = "NOT ${that.reason}",
                description = "NOT ${that.description}",
                identification = "NOT (${that.identification})",
                children = emptyList()
        )
    }
}
