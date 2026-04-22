package br.com.dillmann.fireflycompanion.business.transaction.usecase

interface SuggestTransactionCateogoryUseCase {
    suspend fun suggest(description: String, excludeTransactionId: String?): String?
}
