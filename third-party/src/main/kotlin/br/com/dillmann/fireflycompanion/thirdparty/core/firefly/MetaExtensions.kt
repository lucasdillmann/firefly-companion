package br.com.dillmann.fireflycompanion.thirdparty.core.firefly

import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.Meta

internal fun <I, O> Meta?.toPage(items: List<I>, converter: (I) -> O): Page<O> =
    Page(
        currentPage = this?.pagination?.currentPage ?: 0,
        totalPages = this?.pagination?.totalPages ?: 0,
        pageSize = this?.pagination?.perPage ?: 0,
        content = items.map(converter),
    )
