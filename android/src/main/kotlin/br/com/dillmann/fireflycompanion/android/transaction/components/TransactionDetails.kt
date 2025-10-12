package br.com.dillmann.fireflycompanion.android.transaction.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.emptyVolatile
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.currency.usecase.GetDefaultCurrencyUseCase
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.business.transaction.usecase.DeleteTransactionUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SaveTransactionUseCase
import br.com.dillmann.fireflycompanion.core.validation.ConsistencyException
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome
import java.math.BigDecimal
import java.time.OffsetDateTime

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransactionDetails(
    transaction: Transaction?,
    finish: () -> Unit,
) {
    val editMode = transaction?.id != null
    val scrollState = rememberScrollState()
    val validationOutcome = volatile<ValidationOutcome?>(null)
    val showLoading = volatile(false)
    val showDeleteConfirmation = volatile(false)
    val description = volatile(TextFieldValue(transaction?.description ?: ""))
    val amount = volatile(transaction?.amount ?: BigDecimal.ZERO)
    val category = volatile(TextFieldValue(transaction?.category ?: ""))
    val dateTime = volatile(transaction?.date ?: OffsetDateTime.now())
    val sourceAccount = volatile(TextFieldValue(transaction?.sourceAccountName ?: ""))
    val destinationAccount = volatile(TextFieldValue(transaction?.destinationAccountName ?: ""))
    val transactionType = volatile(transaction?.type ?: Transaction.Type.WITHDRAWAL)
    val tag = volatile(TextFieldValue(transaction?.tags?.firstOrNull() ?: ""))
    val currency = emptyVolatile<Currency>()

    fun handleSave() {
        val saveAction = get<SaveTransactionUseCase>()
        val getCurrencyAction = get<GetDefaultCurrencyUseCase>()

        validationOutcome.value = null
        showLoading.value = true

        async {
            try {
                val updatedTransaction = Transaction(
                    id = transaction?.id,
                    description = description.value.text,
                    category = category.value.text.takeIf { it.isNotBlank() },
                    date = dateTime.value,
                    amount = amount.value,
                    currency = transaction?.currency ?: getCurrencyAction.getDefault(),
                    type = transactionType.value,
                    sourceAccountName = sourceAccount.value.text.takeIf { it.isNotBlank() },
                    destinationAccountName = destinationAccount.value.text.takeIf { it.isNotBlank() },
                    tags = setOfNotNull(tag.value.text.takeIf { it.isNotBlank() }),
                )

                saveAction.save(updatedTransaction)
                finish()
            } catch (ex: ConsistencyException) {
                validationOutcome.value = ex.outcome
            } finally {
                showLoading.value = false
            }
        }
    }

    fun handleDelete() {
        val deleteAction = get<DeleteTransactionUseCase>()
        val id = transaction?.id ?: return

        showLoading.value = true

        async {
            try {
                deleteAction.delete(id)
                finish()
            } finally {
                showLoading.value = false
            }
        }
    }

    LaunchedEffect(Unit) {
        async {
            showLoading.value = true
            currency.value = get<GetDefaultCurrencyUseCase>().getDefault()
            showLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                title = {
                    val key =
                        if (editMode) R.string.edit_transaction
                        else R.string.new_transaction
                    Text(text = i18n(key))
                },
                actions = {
                    if (editMode) {
                        IconButton(
                            onClick = { showDeleteConfirmation.value = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = i18n(R.string.delete)
                            )
                        }
                    }

                    Button(
                        onClick = { handleSave() },
                        enabled = transaction?.type !== Transaction.Type.RECONCILIATION,
                    ) {
                        Text(i18n(R.string.save))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .imePadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            if (currency.value != null) {
                TransactionFormFields(
                    description = description,
                    amount = amount,
                    category = category,
                    dateTime = dateTime,
                    sourceAccount = sourceAccount,
                    destinationAccount = destinationAccount,
                    transactionType = transactionType,
                    validationOutcome = validationOutcome.value,
                    tag = tag,
                    currency = currency.value!!,
                )
            }
        }
    }

    if (showLoading.value) {
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

    if (showDeleteConfirmation.value) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation.value = false },
            title = { Text(i18n(R.string.delete_transaction)) },
            text = { Text(i18n(R.string.delete_transaction_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation.value = false
                        handleDelete()
                    }
                ) {
                    Text(i18n(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation.value = false }
                ) {
                    Text(i18n(R.string.cancel))
                }
            }
        )
    }
}
