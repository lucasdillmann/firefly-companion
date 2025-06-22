package br.com.dillmann.fireflycompanion.core.validation

class ValidationOutcome {
    data class Violation(
        val property: String?,
        val message: String,
    )

    var success = true
        private set
    var violations = emptyList<Violation>()
        private set

    fun addViolation(property: String, message: String) {
        violations += Violation(property, message)
        success = false
    }

    fun throwExceptionIfNeeded() =
        if (!success) throw ConsistencyException(this) else Unit
}
