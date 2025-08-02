package br.com.dillmann.fireflycompanion.android.transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.autocomplete.AutoCompleteOutlinedTextField
import br.com.dillmann.fireflycompanion.android.core.components.datepicker.DatePicker
import br.com.dillmann.fireflycompanion.android.core.components.datepicker.DatePickerType
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.business.autocomplete.AutoCompleteType
import br.com.dillmann.fireflycompanion.business.autocomplete.usecase.AutoCompleteUseCase
import br.com.dillmann.fireflycompanion.business.transaction.Transaction.Type
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome
import java.time.OffsetDateTime

@Composable
fun TransactionFormFields(
    description: MutableState<TextFieldValue>,
    amount: MutableState<TextFieldValue>,
    category: MutableState<TextFieldValue>,
    dateTime: MutableState<OffsetDateTime>,
    sourceAccount: MutableState<TextFieldValue>,
    destinationAccount: MutableState<TextFieldValue>,
    transactionType: MutableState<Type>,
    validationOutcome: ValidationOutcome?,
) {
    val autoComplete = koin().get<AutoCompleteUseCase>()
    val supportedTypes = listOf(Type.DEPOSIT, Type.WITHDRAWAL, Type.TRANSFER)
    val sourceAccountRequiredTypes = listOf(Type.TRANSFER, Type.WITHDRAWAL, Type.RECONCILIATION)
    val destinationAccountRequiredTypes = listOf(Type.TRANSFER, Type.DEPOSIT, Type.RECONCILIATION)
    val disabled = transactionType.value !in supportedTypes

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        supportedTypes.forEach { type ->
            FilterChip(
                selected = transactionType.value == type,
                onClick = { transactionType.value = type },
                label = { Text(type.name) },
                enabled = !disabled,
            )
        }
    }

    AutoCompleteOutlinedTextField(
        value = description,
        label = i18n(R.string.description),
        modifier = Modifier.fillMaxWidth(),
        disabled = disabled,
        isError = validationOutcome?.messageFor("description") != null,
        suggestionsProvider = {
            autoComplete.getSuggestions(AutoCompleteType.DESCRIPTION, it)
        },
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
        enabled = !disabled,
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

    DatePicker(
        dateTimeState = dateTime,
        type = DatePickerType.DATE_ONLY,
        label = i18n(R.string.date),
        enabled = !disabled,
        supportingText = {
            val message = validationOutcome?.messageFor("dateTime")
            if (message != null) {
                Text(
                    text = message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    )

    AutoCompleteOutlinedTextField(
        value = category,
        label = i18n(R.string.category),
        modifier = Modifier.fillMaxWidth(),
        disabled = disabled,
        supportingText = { null },
        suggestionsProvider = {
            autoComplete.getSuggestions(AutoCompleteType.CATEGORY, it)
        },
    )

    if (transactionType.value in sourceAccountRequiredTypes) {
        AutoCompleteOutlinedTextField(
            value = sourceAccount,
            label = i18n(R.string.source_account),
            modifier = Modifier.fillMaxWidth(),
            disabled = disabled,
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
            },
            suggestionsProvider = {
                autoComplete.getSuggestions(AutoCompleteType.ACCOUNT, it)
            },
        )
    }

    if (transactionType.value in destinationAccountRequiredTypes) {
        AutoCompleteOutlinedTextField(
            value = destinationAccount,
            label = i18n(R.string.destination_account),
            modifier = Modifier.fillMaxWidth(),
            disabled = disabled,
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
            },
            suggestionsProvider = {
                autoComplete.getSuggestions(AutoCompleteType.ACCOUNT, it)
            },
        )
    }
}
