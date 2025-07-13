package br.com.dillmann.fireflycompanion.core.pagination

data class Page<T>(
    val currentPage: Int,
    val totalPages: Int,
    val pageSize: Int,
    val content: List<T>
) : Iterable<T> by content {
    companion object {
        fun <T> empty() = Page<T>(0, 0, 0, emptyList())
    }

    fun filter(predicate: (T) -> Boolean) = copy(content = content.filter(predicate))
}
