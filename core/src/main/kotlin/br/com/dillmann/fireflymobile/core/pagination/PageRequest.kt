package br.com.dillmann.fireflymobile.core.pagination

data class PageRequest(
    val size: Int,
    val number: Int,
) {
    companion object {
        val DEFAULT = PageRequest(size = 10, number = 0)
    }
}
