package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.Transaction.Type
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome
import java.math.BigDecimal

internal class TransactionValidator {
    private companion object {
        private val TYPES_THAT_REQUIRE_SOURCE_ACCOUNT = listOf(Type.WITHDRAWAL, Type.TRANSFER)
        private val TYPES_THAT_REQUIRE_DESTINATION_ACCOUNT = listOf(Type.DEPOSIT, Type.TRANSFER)
    }

    fun validate(transaction: Transaction) {
        val outcome = ValidationOutcome()

        with(transaction) {
            if (description.isBlank())
                outcome.addViolation(::description.name, "Description is required")

            if (amount <= BigDecimal.ZERO)
                outcome.addViolation(::amount.name, "Amount should not be zero or less")

            if (sourceAccountName.isNullOrBlank() && type in TYPES_THAT_REQUIRE_SOURCE_ACCOUNT)
                outcome.addViolation(::sourceAccountName.name, "Source account is required")

            if (destinationAccountName.isNullOrBlank() && type in TYPES_THAT_REQUIRE_DESTINATION_ACCOUNT)
                outcome.addViolation(::destinationAccountName.name, "Destination account is required")
        }

        outcome.throwExceptionIfNeeded()
    }
}
