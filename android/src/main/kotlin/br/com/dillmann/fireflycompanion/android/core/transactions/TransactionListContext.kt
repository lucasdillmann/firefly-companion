package br.com.dillmann.fireflycompanion.android.core.transactions

interface TransactionListContext {
    fun isLoading(): Boolean
    fun containsData(): Boolean
    fun refresh()
}
