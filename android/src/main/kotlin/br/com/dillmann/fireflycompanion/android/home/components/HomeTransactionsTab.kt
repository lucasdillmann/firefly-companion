package br.com.dillmann.fireflycompanion.android.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.core.activity.state
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyVisibilityToggle
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefresh
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.transaction.TransactionFormActivity
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTransactionsTab() {
    val listUseCase = koin().get<ListTransactionsUseCase>()
    val searchUseCase = koin().get<SearchTransactionsUseCase>()

    var transactions by state(emptyList<Transaction>())
    var currentPage by state(0)
    val listState = rememberLazyListState()
    var loadTask by state<CompletableFuture<Any>?>(null)
    var searchTerms by state("")

    fun loadTransactions(page: Int = 0, refresh: Boolean = false) {
        loadTask?.cancel(true)
        if (refresh) {
            currentPage = 0
            transactions = emptyList()
        }

        loadTask = async {
            try {
                val page = PageRequest(number = page)
                transactions +=
                    if (searchTerms.isBlank()) listUseCase.list(page)
                    else searchUseCase.search(page, searchTerms)
            } catch (e: Exception) {
                Logger
                    .getLogger("HomeTransactionsTab")
                    .warning("Error loading transactions: ${e.stackTrace}")
            } finally {
                loadTask = null
            }
        }
    }

    LaunchedEffect(Unit) {
        loadTransactions()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && loadTask.done()) {
                    val totalItems = transactions.size
                    if (lastVisibleIndex >= totalItems - 3 && totalItems > 0) {
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
            SearchField(
                searchTerms = searchTerms,
                enabled = loadTask.done(),
                onChange = { searchTerms = it },
                refresh = { loadTransactions(refresh = true) },
            )

            if (loadTask.done() && transactions.isEmpty()) {
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
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                    }

                    if (!loadTask.done()) {
                        item {
                            LoadingIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    searchTerms: String,
    enabled: Boolean,
    onChange: (String) -> Unit,
    refresh: () -> Unit,
) {
    TextField(
        value = searchTerms,
        onValueChange = { onChange(it.replace("\n", "")) },
        label = { Text(text = i18n(R.string.search)) },
        enabled = enabled,
        modifier = Modifier
            .padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            Button(
                onClick = refresh,
                enabled = enabled,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = i18n(R.string.search),
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { refresh() },
            onDone = { refresh() },
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
    )
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val context = LocalContext.current
    val (icon, tint) = when (transaction.type) {
        Transaction.Type.WITHDRAWAL -> Icons.Default.ArrowUpward to Color(0xFFF44336)
        Transaction.Type.DEPOSIT -> Icons.Default.ArrowDownward to Color(0xFF4CAF50)
        Transaction.Type.TRANSFER -> Icons.Default.SwapHoriz to Color(0xFF2196F3)
        else -> Icons.Default.QuestionMark to Color.Unspecified
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            context.start<TransactionFormActivity>(
                extras = mapOf("transaction" to transaction)
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = transaction.type.name,
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val text =
                        when {
                            transaction.type == Transaction.Type.TRANSFER ->
                                "${transaction.sourceAccountName} → ${transaction.destinationAccountName}"

                            transaction.category != null ->
                                transaction.category!!

                            else ->
                                ""

                        }

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column {
                MoneyText(
                    value = transaction.amount,
                    currency = transaction.currency,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = tint,
                    horizontalArrangement = Arrangement.End,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = transaction.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

private fun CompletableFuture<out Any>?.done() =
    this == null || isDone
