package br.com.dillmann.fireflycompanion.android.core.components.transactions

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefresh
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.Page
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransactionList(
    showAccountNameOnReconciliation: Boolean,
    modifier: Modifier = Modifier,
    header: (@Composable (Boolean) -> Unit)? = null,
    transactionsProvider: suspend (PageRequest) -> Page<Transaction>,
    dateFormat: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM),
) {
    val queue by persistent(ActionQueue())
    var items by persistent(emptyList<Any>())
    var currentPage by persistent(0)
    var hasMorePages by persistent(true)
    var lastListDate by persistent<LocalDate?>(null)
    val listState = rememberLazyListState()
    var loading by persistent(false)

    fun loadTransactions(pageNumber: Int = 0, refresh: Boolean = false) {
        queue.add {
            loading = true
            if (refresh) {
                currentPage = 0
                hasMorePages = true
                items = emptyList()
                lastListDate = null
            }

            try {
                val pageRequest = PageRequest(pageNumber)
                val page = transactionsProvider(pageRequest)
                val (enrichedList, lastPageDate) = enrichWithDates(page.content, lastListDate)

                items += enrichedList
                lastListDate = lastPageDate
                hasMorePages = page.hasMorePages
            } catch (e: Exception) {
                Log.w("TransactionsList", "Error loading transactions", e)
            } finally {
                loading = false
            }
        }
    }

    OnRefreshEvent("TransactionList", TransactionListScope) {
        loadTransactions(refresh = true)
    }

    LaunchedEffect(Unit) {
        if (items.isEmpty() && !loading)
            loadTransactions()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && !loading) {
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
        enabled = !loading,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (header != null) {
                header(!loading)
            }

            if (!loading && items.isEmpty()) {
                Text(
                    text = i18n(R.string.no_data_yet),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(top = 96.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = i18n(R.string.click_the_button_bellow_to_add),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items) {
                        when (it) {
                            is Transaction ->
                                TransactionListItem(
                                    transaction = it,
                                    showAccountNameOnReconciliation = showAccountNameOnReconciliation,
                                )

                            is LocalDate -> {
                                val topPadding =
                                    if (items.indexOf(it) == 0) 0
                                    else 16

                                Text(
                                    text = dateFormat.format(it),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(top = topPadding.dp, bottom = 8.dp)
                                        .fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Start,
                                )
                            }
                        }
                    }

                    if (loading) {
                        item {
                            TransactionListLoadingIndicator()
                        }
                    }
                }
            }
        }
    }
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
