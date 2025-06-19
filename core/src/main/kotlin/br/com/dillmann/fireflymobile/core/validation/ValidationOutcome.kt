package br.com.dillmann.fireflymobile.core.validation

data class ValidationOutcome(
    val success: Boolean,
    val violations: List<Violation>,
) {
    data class Violation(
        val property: String?,
        val message: String,
    )
}
