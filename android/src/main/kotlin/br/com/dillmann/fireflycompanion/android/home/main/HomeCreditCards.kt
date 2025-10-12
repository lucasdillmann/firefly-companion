package br.com.dillmann.fireflycompanion.android.home.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.loading.LoadingIndicator
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.components.section.SectionCard
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.android.core.refresh.OnRefreshEvent
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.router.navigate
import br.com.dillmann.fireflycompanion.android.home.HomeTabs
import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.core.pagination.fetchAllPages
import java.math.BigDecimal
import java.time.OffsetDateTime

@Composable
fun HomeCreditCards() {
    val actionQueue by persistent(ActionQueue())
    var creditCards by persistent(::fetchCreditCards)

    OnRefreshEvent("HomeCreditCards", HomeTabs.MAIN) {
        actionQueue.add {
            creditCards = null
            creditCards = fetchCreditCards()
        }
    }

    SectionCard(
        title = i18n(R.string.credit_cards),
        targetState = creditCards,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (creditCards == null) {
                LoadingIndicator()
            } else {
                CreditCardList(creditCards!!)
            }
        }
    }
}

@Composable
private fun CreditCardList(creditCards: List<Account>) {
    if (creditCards.isEmpty()) {
        Text(
            text = i18n(R.string.no_data_yet),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        creditCards.forEachIndexed{ index, creditCard ->
            CreditCardDetails(creditCard)

            if (index < creditCards.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 0.dp),
                    thickness = 0.2.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun CreditCardDetails(creditCard: Account) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.CreditCard,
                contentDescription = i18n(R.string.credit_cards),
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = { openAccountForm(creditCard) }),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .clickable(onClick = { openAccountForm(creditCard) }),
        ) {
            Text(
                text = creditCard.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            MoneyText(
                value = creditCard.currentBalance.abs(),
                currency = creditCard.currency,
                dynamicColors = false,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        IconButton(
            onClick = { openTransactionsForm(creditCard) },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = i18n(R.string.new_transaction),
            )
        }
    }
}

private fun openTransactionsForm(account: Account) {
    val transaction =
        Transaction(
            id = null,
            description = "",
            category = null,
            date = OffsetDateTime.now(),
            amount = BigDecimal.ZERO,
            currency = account.currency,
            type = Transaction.Type.WITHDRAWAL,
            sourceAccountName = account.name,
            destinationAccountName = null,
            tags = emptySet(),
        )

    navigate(Route.TRANSACTION_FORM, transaction)
}

private fun openAccountForm(account: Account) {
    navigate(Route.ACCOUNT_FORM, account)
}

private suspend fun fetchCreditCards(): List<Account> {
    val useCase = get<ListAccountsUseCase>()
    return fetchAllPages { useCase.listAccounts(it) }
        .filter { it.role == Account.Role.CREDIT_CARD }
}
