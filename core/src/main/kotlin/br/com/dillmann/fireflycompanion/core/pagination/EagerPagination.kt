package br.com.dillmann.fireflycompanion.core.pagination

suspend fun <T> fetchAllPages(
    initialPage: PageRequest = PageRequest(),
    pageProvider: suspend (page: PageRequest) -> Page<T>
): List<T> {
    val result = mutableListOf<T>()
    var currentPage = initialPage

    do {
       val page = pageProvider(currentPage)
       result += page.content

       if (page.hasMorePages)
           currentPage = currentPage.copy(number = currentPage.number + 1)
    } while(page.hasMorePages)

    return result
}
