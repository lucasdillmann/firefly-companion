package br.com.dillmann.fireflycompanion.android.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.components.transactions.TransactionList
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.refresh.RefreshDispatcher
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.UpdateAccountBalanceUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import org.koin.java.KoinJavaComponent.getKoin
import java.math.BigDecimal

private val queue = ActionQueue()

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NavigationContext.AccountForm() {
    var account by volatile(requireBagValue<Account>())
    var balanceState by volatile(TextFieldValue(account.currentBalance.toString()))
    var showLoading by volatile(false)
    val listTransactionsUseCase = getKoin().get<ListTransactionsUseCase>()
    val updateBalanceUseCase = getKoin().get<UpdateAccountBalanceUseCase>()
    val getAccountUseCase = getKoin().get<GetAccountUseCase>()

    fun updateBalance() {
        showLoading = true

        async {
            val newBalance = BigDecimal(balanceState.text)
            updateBalanceUseCase.updateBalance(account.id, newBalance)

            RefreshDispatcher.notify()
        }
    }

    OnRefreshEvent {
        queue.add {
            showLoading = true
            account = getAccountUseCase.getAccount(account.id)!!
            showLoading = false
        }
    }

    LaunchedEffect(account) {
        balanceState = TextFieldValue(account.currentBalance.toString())
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
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                value = balanceState,
                onValueChange = { balanceState = it },
                label = { Text(i18n(R.string.balance)) },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = LocalTextStyle.current.fontSize.times(2),
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            Section(
                title = i18n(R.string.tab_transactions),
            ) {
                 TransactionList(
                    showAccountNameOnReconciliation = false,
                    transactionsProvider = { listTransactionsUseCase.list(page = it, accountId = account.id) },
                )
            }
        }
    }

    if (showLoading) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = i18n(R.string.loading))
            }
        }
    }
}
