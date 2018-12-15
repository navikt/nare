package no.nav.knare.core.evaluations

data class Evaluation(val result: Result,
                      val reason: String,
                      val description: String?,
                      val identification: String) {

    fun and(other: Evaluation) = Evaluation(
            result = this.result.and(other.result),
            reason = "${this.reason} AND ${other.reason}",
            description = "${this.description ?: "no description"} AND ${other.description ?: "no description"}",
            identification = "${this.identification} AND ${other.identification}"
    )

    fun or(other: Evaluation) = Evaluation(
            result = this.result.or(other.result),
            reason = "${this.reason} OR ${other.reason}",
            description = "${this.description ?: "no description"} OR ${other.description ?: "no description"}",
            identification = "${this.identification} OR ${other.identification}"
    )

    fun not() = Evaluation(
            result = this.result.not(),
            reason = "NOT ${this.reason}",
            description = "NOT ${this.description}",
            identification = "NOT (${this.identification})"
    )

    companion object {
        fun ja(reason: String, description: String, identification: String) =
                Evaluation(Result.YES, reason, description, identification)

        fun nei(reason: String, description: String, identification: String) =
                Evaluation(Result.NO, reason, description, identification)

        fun kanskje(reason: String, description: String, identification: String) =
                Evaluation(Result.MAYBE, reason, description, identification)
    }
}
