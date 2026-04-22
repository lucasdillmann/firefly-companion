package br.com.dillmann.fireflycompanion.android.core.components.action

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ClientError
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ClientException
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ServerError

@Composable
fun LoadErrorDialog(
    exception: Exception,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
) {
    var showDetails by volatile(false)

    LaunchedEffect(exception) {
        val summary = exception.message?.takeIf { it.isNotEmpty() } ?: exception.toString()
        Log.w("LoadErrorDialog", "Error modal shown: $summary", exception)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = i18n(R.string.error_action_failed))
        },
        text = {
            LoadErrorDetails(showDetails, exception)
        },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(text = i18n(R.string.try_again))
            }
        },
        dismissButton = {
            TextButton(onClick = { showDetails = !showDetails }) {
                Text(
                    text =
                        if (showDetails) i18n(R.string.hide_details)
                        else i18n(R.string.show_details),
                )
            }
        },
    )
}

@Composable
private fun LoadErrorDetails(
    techDetailsVisible: Boolean,
    exception: Exception,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = exception.message ?: i18n(R.string.error_unexpected))

        if (!techDetailsVisible)
            return@Column

        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scroll),
        ) {
            if (exception is ClientException) {
                val response = exception.response
                val responseBody =
                    when (response) {
                        is ServerError<*> -> response.body?.toString()
                        is ClientError<*> -> response.body?.toString()
                        else -> null
                    }

                if (responseBody != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = responseBody,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = exception.stackTraceToString(),
            )
        }
    }
}
