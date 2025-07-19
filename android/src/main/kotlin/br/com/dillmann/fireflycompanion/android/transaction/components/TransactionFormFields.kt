package br.com.dillmann.fireflycompanion.android.transaction.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.business.transaction.Transaction.Type
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome

@Composable
fun TransactionFormFields(
    description: MutableState<TextFieldValue>,
    amount: MutableState<TextFieldValue>,
    category: MutableState<TextFieldValue>,
    sourceAccount: MutableState<TextFieldValue>,
    destinationAccount: MutableState<TextFieldValue>,
    transactionType: MutableState<Type>,
    validationOutcome: ValidationOutcome?
) {
    val supportedTypes = listOf(Type.DEPOSIT, Type.WITHDRAWAL, Type.TRANSFER)
    val sourceAccountRequiredTypes = listOf(Type.TRANSFER, Type.WITHDRAWAL)
    val destinationAccountRequiredTypes = listOf(Type.TRANSFER, Type.DEPOSIT)

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        supportedTypes.forEach { type ->
            FilterChip(
                selected = transactionType.value == type,
                onClick = { transactionType.value = type },
                label = { Text(type.name) }
            )
        }
    }

    OutlinedTextField(
        value = description.value,
        onValueChange = { description.value = it },
        label = { Text(text = i18n(R.string.description)) },
        modifier = Modifier.fillMaxWidth(),
        isError = validationOutcome?.messageFor("description") != null,
        supportingText = {
            val message = validationOutcome?.messageFor("description")
            if (message != null) {
                Text(
                    text = message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    )

    OutlinedTextField(
        value = amount.value,
        onValueChange = { amount.value = it },
        label = { Text(text = i18n(R.string.amount)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        isError = validationOutcome?.messageFor("amount") != null,
        supportingText = {
            val message = validationOutcome?.messageFor("amount")
            if (message != null) {
                Text(
                    text = message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    )

    OutlinedTextField(
        value = category.value,
        onValueChange = { category.value = it },
        label = { Text(text = i18n(R.string.category)) },
        modifier = Modifier.fillMaxWidth(),
        supportingText = { null },
    )

    if (transactionType.value in sourceAccountRequiredTypes) {
        OutlinedTextField(
            value = sourceAccount.value,
            onValueChange = { sourceAccount.value = it },
            label = { Text(text = i18n(R.string.source_account)) },
            modifier = Modifier.fillMaxWidth(),
            isError = validationOutcome?.messageFor("sourceAccountName") != null,
            supportingText = {
                val message = validationOutcome?.messageFor("sourceAccountName")
                if (message != null) {
                    Text(
                        text = message,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        )
    }

    if (transactionType.value in destinationAccountRequiredTypes) {
        OutlinedTextField(
            value = destinationAccount.value,
            onValueChange = { destinationAccount.value = it },
            label = { Text(text = i18n(R.string.destination_account)) },
            modifier = Modifier.fillMaxWidth(),
            isError = validationOutcome?.messageFor("destinationAccountName") != null,
            supportingText = {
                val message = validationOutcome?.messageFor("destinationAccountName")
                if (message != null) {
                    Text(
                        text = message,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        )
    }
}
