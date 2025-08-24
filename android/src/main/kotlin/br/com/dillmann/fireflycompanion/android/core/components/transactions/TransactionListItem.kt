package br.com.dillmann.fireflycompanion.android.core.components.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyColor
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.router.navigate
import br.com.dillmann.fireflycompanion.business.transaction.Transaction

@Composable
fun TransactionListItem(
    transaction: Transaction,
    showAccountNameOnReconciliation: Boolean,
) {
    val (icon, tint) = when (transaction.type) {
        Transaction.Type.WITHDRAWAL -> Icons.Default.ArrowUpward to MoneyColor.negative()
        Transaction.Type.DEPOSIT -> Icons.Default.ArrowDownward to MoneyColor.positive()
        Transaction.Type.TRANSFER -> Icons.Default.SwapHoriz to MoneyColor.internal()
        Transaction.Type.RECONCILIATION -> {
            val icon = Icons.Default.Build
            if (transaction.destinationAccountName!!.contains("reconciliation"))
                icon to MoneyColor.negative()
            else
                icon to MoneyColor.positive()
        }

        else -> Icons.Default.QuestionMark to MoneyColor.base()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { navigate(Route.TRANSACTION_FORM, transaction) },
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

                            transaction.type == Transaction.Type.RECONCILIATION && showAccountNameOnReconciliation ->
                                if (transaction.sourceAccountName!!.contains("reconciliation"))
                                    transaction.destinationAccountName!!
                                else
                                    transaction.sourceAccountName!!

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
                    baseColor = tint,
                    dynamicColors = false,
                    horizontalArrangement = Arrangement.End,
                )
            }
        }
    }
}
