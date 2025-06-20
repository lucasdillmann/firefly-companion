package br.com.dillmann.fireflymobile.thirdparty.core

import br.com.dillmann.fireflymobile.core.pagination.Page
import br.com.dillmann.fireflymobile.firefly.models.Meta

internal fun <I, O> Meta?.toPage(items: List<I>, converter: (I) -> O): Page<O> =
    Page(
        currentPage = this?.pagination?.currentPage ?: 0,
        totalPages = this?.pagination?.totalPages ?: 0,
        pageSize = this?.pagination?.perPage ?: 0,
        items = items.map(converter),
    )
