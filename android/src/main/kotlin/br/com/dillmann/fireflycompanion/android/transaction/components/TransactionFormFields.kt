package br.com.dillmann.fireflycompanion.android.transaction.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import br.com.dillmann.fireflycompanion.android.core.components.selectionrow.SelectionRow
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppTextField
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
    tag: MutableState<TextFieldValue>,
) {
    val autoComplete = koin().get<AutoCompleteUseCase>()
    val supportedTypes = listOf(Type.DEPOSIT, Type.WITHDRAWAL, Type.TRANSFER)
    val sourceAccountRequiredTypes = listOf(Type.TRANSFER, Type.WITHDRAWAL, Type.RECONCILIATION)
    val destinationAccountRequiredTypes = listOf(Type.TRANSFER, Type.DEPOSIT, Type.RECONCILIATION)
    val disabled = transactionType.value !in supportedTypes

    SelectionRow(
        options = supportedTypes,
        textRenderer = ::translate,
        enabled = !disabled,
        initialSelection = transactionType.value,
        onOptionSelected = { transactionType.value = it },
        modifier = Modifier.padding(start = 0.dp, bottom = 8.dp),
    )

    AutoCompleteOutlinedTextField(
        value = description,
        label = i18n(R.string.description),
        modifier = Modifier.fillMaxWidth(),
        disabled = disabled,
        errorMessage = validationOutcome?.messageFor("description"),
        suggestionsProvider = {
            autoComplete.getSuggestions(AutoCompleteType.DESCRIPTION, it)
        },
    )

    AppTextField(
        value = amount.value,
        onChange = { amount.value = it },
        label = i18n(R.string.amount),
        modifier = Modifier.fillMaxWidth(),
        enabled = !disabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        errorMessage = validationOutcome?.messageFor("amount"),
    )

    DatePicker(
        dateTimeState = dateTime,
        type = DatePickerType.DATE_ONLY,
        label = i18n(R.string.date),
        enabled = !disabled,
        errorMessage = validationOutcome?.messageFor("dateTime"),
    )

    AutoCompleteOutlinedTextField(
        value = category,
        label = i18n(R.string.category),
        modifier = Modifier.fillMaxWidth(),
        disabled = disabled,
        errorMessage = validationOutcome?.messageFor("category"),
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
            errorMessage = validationOutcome?.messageFor("sourceAccountName"),
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
            errorMessage = validationOutcome?.messageFor("destinationAccountName"),
            suggestionsProvider = {
                autoComplete.getSuggestions(AutoCompleteType.ACCOUNT, it)
            },
        )
    }

    AutoCompleteOutlinedTextField(
        value = tag,
        label = i18n(R.string.tag),
        modifier = Modifier.fillMaxWidth(),
        disabled = disabled,
        errorMessage = validationOutcome?.messageFor("tag"),
        suggestionsProvider = {
            autoComplete.getSuggestions(AutoCompleteType.TAG, it)
        },
    )
}

private fun translate(type: Type) =
    when(type) {
        Type.DEPOSIT -> R.string.deposit
        Type.WITHDRAWAL -> R.string.withdrawal
        Type.TRANSFER -> R.string.transfer
        else -> error("Unsupported type: $type")
    }.let {
        i18n(it)
    }
