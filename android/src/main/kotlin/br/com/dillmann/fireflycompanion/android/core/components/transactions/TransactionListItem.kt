package br.com.dillmann.fireflycompanion.android.core.components.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.core.components.money.MoneyText
import br.com.dillmann.fireflycompanion.android.core.theme.Colors
import br.com.dillmann.fireflycompanion.android.home.tabs.HomeTabs
import br.com.dillmann.fireflycompanion.android.transaction.TransactionFormActivity
import br.com.dillmann.fireflycompanion.business.transaction.Transaction

@Composable
fun TransactionListItem(
    transaction: Transaction,
    showAccountNameOnReconciliation: Boolean,
) {
    val context = LocalContext.current
    val (icon, tint) = when (transaction.type) {
        Transaction.Type.WITHDRAWAL -> Icons.Default.ArrowUpward to Colors.RED
        Transaction.Type.DEPOSIT -> Icons.Default.ArrowDownward to Colors.GREEN
        Transaction.Type.TRANSFER -> Icons.Default.SwapHoriz to Colors.BLUE
        Transaction.Type.RECONCILIATION -> {
            val icon = Icons.Default.Build
            if (transaction.destinationAccountName!!.contains("reconciliation"))
                icon to Colors.RED
            else
                icon to Colors.GREEN
        }

        else -> Icons.Default.QuestionMark to Color.Unspecified
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            context.start<TransactionFormActivity>(
                extras = mapOf("transaction" to transaction),
                requestCode = HomeTabs.TRANSACTIONS.ordinal,
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
                    color = tint,
                    horizontalArrangement = Arrangement.End,
                )
            }
        }
    }
}
