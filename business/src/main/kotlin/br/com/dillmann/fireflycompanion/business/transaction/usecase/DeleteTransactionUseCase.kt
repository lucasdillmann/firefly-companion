package br.com.dillmann.fireflycompanion.business.transaction.usecase

interface DeleteTransactionUseCase {
    suspend fun delete(id: String)
}
