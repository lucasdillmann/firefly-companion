package br.com.dillmann.fireflycompanion.android.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.action.AsyncAction
import br.com.dillmann.fireflycompanion.android.core.components.action.AsyncActionSink
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppMoneyTextField
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppTextFieldDefaults
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionList
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.UpdateAccountBalanceUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NavigationContext.AccountForm() {
    val actionSink by volatile(AsyncActionSink())
    var account by volatile(requireBagValue<Account>())
    var balance by volatile(account.currentBalance)
    val listTransactionsUseCase = get<ListTransactionsUseCase>()
    val updateBalanceUseCase = get<UpdateAccountBalanceUseCase>()
    val getAccountUseCase = get<GetAccountUseCase>()

    fun updateBalance() {
        actionSink.push {
            updateBalanceUseCase.updateBalance(account.id, balance)
            RefreshDispatcher.notify()
        }
    }

    OnRefreshEvent("AccountForm") {
        actionSink.push {
            account = getAccountUseCase.getAccount(account.id)!!
        }
    }

    LaunchedEffect(account) {
        balance = account.currentBalance
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                title = {
                    Text(text = account.name)
                },
                actions = {
                    Button(
                        onClick = { updateBalance() }
                    ) {
                        Text(i18n(R.string.save))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding(),
        ) {
            AppMoneyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 0.dp),
                value = balance,
                onChange = { balance = it },
                textStyle = AppTextFieldDefaults.textStyle.copy(
                    textAlign = TextAlign.Center,
                    fontSize = LocalTextStyle.current.fontSize.times(2),
                ),
                currency = account.currency,
            )

            Section(
                title = i18n(R.string.tab_transactions),
            ) {
                TransactionList(
                    showAccountNameOnReconciliation = false,
                    transactionsProvider = {
                        listTransactionsUseCase.list(page = it, accountId = account.id)
                    },
                )
            }
        }
    }

    AsyncAction(
        sink = actionSink
    )
}
