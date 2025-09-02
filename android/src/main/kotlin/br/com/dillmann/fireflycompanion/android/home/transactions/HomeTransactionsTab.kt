package br.com.dillmann.fireflycompanion.android.home.transactions

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionList
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionListScope
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
import br.com.dillmann.fireflycompanion.android.home.HomeTopActions
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTransactionsTab(
    modifier: Modifier = Modifier,
) {
    val listUseCase = koin().get<ListTransactionsUseCase>()
    val searchUseCase = koin().get<SearchTransactionsUseCase>()
    val searchTerms = volatile(TextFieldValue(""))


    Section(
        title = i18n(R.string.tab_transactions),
        rightContent = {
            HomeTopActions()
        }
    ) {
        TransactionList(
            showAccountNameOnReconciliation = true,
            modifier = modifier,
            header = { ready ->
                HomeTransactionsSearchField(
                    searchTerms = searchTerms,
                    enabled = ready,
                    refresh = { RefreshDispatcher.notify(TransactionListScope) },
                )
            },
            transactionsProvider = {
                val terms = searchTerms.value.text

                if (terms.isBlank()) listUseCase.list(it)
                else searchUseCase.search(it, terms)
            }
        )
    }

    BackHandler(enabled = searchTerms.value.text.isNotBlank()) {
        searchTerms.value = searchTerms.value.copy(text = "")
        RefreshDispatcher.notify(TransactionListScope)
    }
}
