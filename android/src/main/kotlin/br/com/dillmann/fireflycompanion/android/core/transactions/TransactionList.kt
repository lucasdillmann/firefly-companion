package br.com.dillmann.fireflycompanion.android.core.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.state
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibilityToggle
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefresh
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.extensions.cancel
import br.com.dillmann.fireflycompanion.android.core.extensions.done
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger
import kotlin.collections.plus

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransactionList(
    header: @Composable ((TransactionListContext) -> Unit)? = null,
    transactionsProvider: suspend (PageRequest) -> Page<Transaction>,
    dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy"),
): TransactionListContext {
    var items by state(emptyList<Any>())
    var currentPage by state(0)
    var hasMorePages by state(true)
    var lastListDate by state<LocalDate?>(null)
    val listState = rememberLazyListState()
    var loadTask by state<CompletableFuture<Any>?>(null)

    fun loadTransactions(pageNumber: Int = 0, refresh: Boolean = false) {
        loadTask.cancel()
        if (refresh) {
            currentPage = 0
            hasMorePages = true
            items = emptyList()
            lastListDate = null
        }

        loadTask = async {
            try {
                val pageRequest = PageRequest(pageNumber)
                val page = transactionsProvider(pageRequest)
                val (enrichedList, lastPageDate) = enrichWithDates(page.content, lastListDate)

                items += enrichedList
                lastListDate = lastPageDate
                hasMorePages = page.hasMorePages
            } catch (e: Exception) {
                Logger
                    .getLogger("TransactionsList")
                    .warning("Error loading transactions: ${e.stackTrace}")
            } finally {
                loadTask = null
            }
        }
    }

    val context = object: TransactionListContext {
        override fun isLoading() = !loadTask.done()
        override fun refresh() = loadTransactions(refresh = true)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && loadTask.done()) {
                    val totalItems = items.size
                    if (lastVisibleIndex >= totalItems - 3 && totalItems > 0 && hasMorePages) {
                        currentPage++
                        loadTransactions(currentPage)
                    }
                }
            }
    }

    PullToRefresh(
        onRefresh = { loadTransactions(refresh = true) },
        enabled = loadTask.done(),
        modifier = Modifier.fillMaxSize(),
    ) {
        Section(
            title = i18n(R.string.tab_transactions),
            rightContent = {
                MoneyVisibilityToggle()
            }
        ) {
            if (header != null)
                header(context)

            if (loadTask.done() && items.isEmpty()) {
                Text(
                    text = i18n(R.string.not_implemented),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items) {
                        when (it) {
                            is Transaction ->
                                TransactionListItem(it)
                            is LocalDate -> {
                                val topPadding =
                                    if (items.indexOf(it) == 0) 0
                                    else 16

                                Text(
                                    text = dateFormat.format(it),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = topPadding.dp, bottom = 8.dp).fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Start,
                                )
                            }
                        }
                    }

                    if (!loadTask.done()) {
                        item {
                            TransactionListLoadingIndicator()
                        }
                    }
                }
            }
        }
    }

    return context
}

private fun enrichWithDates(
    transactions: List<Transaction>,
    lastDate: LocalDate?,
): Pair<List<Any>, LocalDate?> {
    val distinctDates = setOfNotNull(lastDate).toMutableSet()
    val output = mutableListOf<Any>()

    for (transaction in transactions) {
        val date = transaction.date.toLocalDate()
        if (distinctDates.add(date))
            output += date

        output += transaction
    }

    return output to distinctDates.lastOrNull()
}
