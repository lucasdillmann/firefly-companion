package br.com.dillmann.fireflymobile.core.pagination

data class Page<T>(
    val currentPage: Int,
    val totalPages: Int,
    val pageSize: Int,
    val items: List<T>
) : Iterable<T> by items {
    companion object {
        fun <T> empty() = Page<T>(0, 0, 0, emptyList())
    }

    val hasNextPage = currentPage < totalPages
}
