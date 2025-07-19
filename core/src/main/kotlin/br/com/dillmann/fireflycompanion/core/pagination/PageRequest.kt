package br.com.dillmann.fireflycompanion.core.pagination

data class PageRequest(
    val number: Int = 0,
    val size: Int = 50,
)
