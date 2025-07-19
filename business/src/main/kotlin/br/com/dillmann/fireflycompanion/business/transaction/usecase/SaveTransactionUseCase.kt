package br.com.dillmann.fireflycompanion.business.transaction.usecase

import br.com.dillmann.fireflycompanion.business.transaction.Transaction

interface SaveTransactionUseCase {
    suspend fun save(transaction: Transaction): Transaction
}
