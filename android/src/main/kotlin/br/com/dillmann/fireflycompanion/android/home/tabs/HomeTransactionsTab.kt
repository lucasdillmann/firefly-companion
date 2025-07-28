package br.com.dillmann.fireflycompanion.android.home.tabs

import android.app.Activity.RESULT_OK
import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.activity.result.ResultNotifier
import br.com.dillmann.fireflycompanion.android.core.activity.persistent
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionList
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionListContext
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.home.components.HomeTopActions
import br.com.dillmann.fireflycompanion.android.home.components.HomeTransactionsSearchField
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeTransactionsTab(
    resultNotifier: ResultNotifier,
    modifier: Modifier = Modifier,
) {
    val listUseCase = koin().get<ListTransactionsUseCase>()
    val searchUseCase = koin().get<SearchTransactionsUseCase>()
    var searchTerms by persistent("")
    var listContext: TransactionListContext? = null

    Section(
        title = i18n(R.string.tab_transactions),
        rightContent = {
            HomeTopActions()
        }
    ) {
        listContext = TransactionList(
            showAccountNameOnReconciliation = true,
            modifier = modifier,
            header = { context ->
                HomeTransactionsSearchField(
                    searchTerms = searchTerms,
                    enabled = !context.isLoading(),
                    onChange = { searchTerms = it },
                    refresh = context::refresh,
                )
            },
            transactionsProvider = {
                if (searchTerms.isBlank()) listUseCase.list(it)
                else searchUseCase.search(it, searchTerms)
            }
        )
    }

    fun handleResult(requestCode: Int, resultCode: Int) {
        if (requestCode == HomeTabs.TRANSACTIONS.ordinal && resultCode == RESULT_OK)
            listContext!!.refresh()
    }

    LaunchedEffect(Unit) {
        resultNotifier.subscribe(::handleResult)

        if (!listContext!!.isLoading() && !listContext.containsData())
            listContext.refresh()
    }

    DisposableEffect(Unit) {
        onDispose { resultNotifier.unsubscribe(::handleResult) }
    }

    BackHandler(enabled = searchTerms.isNotBlank()) {
        searchTerms = ""
        listContext!!.refresh()
    }
}
