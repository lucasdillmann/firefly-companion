package br.com.dillmann.fireflycompanion.android.core.components.transactions

interface TransactionListContext {
    fun isLoading(): Boolean
    fun containsData(): Boolean
    fun refresh()
}
