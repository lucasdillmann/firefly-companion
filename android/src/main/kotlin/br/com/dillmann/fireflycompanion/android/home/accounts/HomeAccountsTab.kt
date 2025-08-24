package br.com.dillmann.fireflycompanion.android.home.accounts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.components.pullrefresh.PullToRefresh
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.router.navigate
import br.com.dillmann.fireflycompanion.android.home.HomeTopActions
import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import br.com.dillmann.fireflycompanion.core.pagination.PageRequest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAccountsTab(
    modifier: Modifier = Modifier,
) {
    val queue by persistent(ActionQueue())
    val listUseCase = koin().get<ListAccountsUseCase>()
    var accounts by persistent(emptyList<Account>())
    var currentPage by persistent(0)
    var hasMorePages by persistent(true)
    var loading by volatile(false)
    val listState = rememberLazyListState()

    fun loadAccounts(pageNumber: Int = 0, refresh: Boolean = false) {
        queue.add {
            loading = true
            if (refresh) {
                hasMorePages = true
                currentPage = 0
                accounts = emptyList()
            }

            try {
                val page = listUseCase.listAccounts(page = PageRequest(pageNumber))
                accounts += page.content
                hasMorePages = page.hasMorePages
            } catch (e: Exception) {
                Log.w("HomeAccountsTab", "Error loading accounts", e)
            } finally {
                loading = false
            }
        }
    }

    OnRefreshEvent {
        loadAccounts(refresh = true)
    }

    LaunchedEffect(Unit) {
        if (accounts.isEmpty() && !loading)
            loadAccounts()
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && !loading) {
                    val totalItems = accounts.size
                    if (lastVisibleIndex >= totalItems - 3 && totalItems > 0 && hasMorePages) {
                        currentPage++
                        loadAccounts(currentPage)
                    }
                }
            }
    }

    PullToRefresh(
        onRefresh = { loadAccounts(refresh = true) },
        enabled = !loading,
        modifier = modifier.fillMaxSize(),
    ) {
        Section(
            title = i18n(R.string.accounts),
            rightContent = {
                HomeTopActions()
            }
        ) {
            if (!loading && accounts.isEmpty()) {
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
                    items(accounts) { transaction ->
                        AccountItem(transaction)
                    }

                    if (loading) {
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
private fun AccountItem(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { navigate(Route.ACCOUNT_FORM, account) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val (icon, typeDescription) = account.type.details()

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = typeDescription,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = typeDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column {
                MoneyText(
                    value = account.currentBalance,
                    currency = account.currency,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    dynamicColors = true,
                    horizontalArrangement = Arrangement.End,
                )
            }
        }
    }
}

private fun Account.Type.details(): Pair<ImageVector, String> =
    when (this) {
        Account.Type.CASH -> Icons.Filled.Money to R.string.cash
        Account.Type.ASSET -> Icons.Filled.AccountBalance to R.string.asset
        Account.Type.EXPENSE -> Icons.Filled.Payment to R.string.expense
        Account.Type.IMPORT -> Icons.Filled.Download to R.string._import
        Account.Type.REVENUE -> Icons.Filled.MonetizationOn to R.string.revenue
        Account.Type.LIABILITY,
        Account.Type.LIABILITIES -> Icons.Filled.Warning to R.string.liability
        Account.Type.INITIAL_MINUS_BALANCE -> Icons.Filled.Remove to R.string.initial_balance
        Account.Type.RECONCILIATION -> Icons.Filled.AccountBalance to R.string.reconciliation
    }.let { (icon, stringId) ->
        icon to i18n(stringId)
    }
