package br.com.dillmann.fireflycompanion.android.core.components.action

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.emptyVolatile
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.core.validation.ConsistencyException
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ClientError
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ClientException
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ServerError

typealias OnComplete = () -> Unit
typealias OnError = (Exception) -> Unit
typealias OnViolation = (ConsistencyException) -> Unit
typealias AsyncActionTask = suspend () -> Unit

@Composable
fun AsyncAction(
    sink: AsyncActionSink,
    waitMessage: String = i18n(R.string.loading),
    onComplete: OnComplete? = null,
    onError: OnError? = null,
    onViolation: OnViolation? = null,
) {
    val queue by persistent(ActionQueue())
    var loading by volatile(false)
    var exception by emptyVolatile<Exception>()
    var lastTask by emptyVolatile<AsyncActionTask>()

    suspend fun launch(task: AsyncActionTask) {
        try {
            task()
            onComplete?.invoke()
        } catch (e: ConsistencyException) {
            onViolation?.invoke(e)
        } catch (ex: Exception) {
            onError?.invoke(ex)
            exception = ex
        } finally {
            loading = false
        }
    }

    LaunchedEffect(Unit) {
        sink.attach { task ->
            loading = true
            lastTask = task

            queue.add {
                launch(task)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            sink.detach()
        }
    }

    if (loading) {
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
                Text(text = waitMessage)
            }
        }
    }

    if (exception != null) {
        var showDetails by volatile(false)

        AlertDialog(
            onDismissRequest = { exception = null },
            title = {
                Text(text = i18n(R.string.error_action_failed))
            },
            text = {
                ErrorDetails(showDetails, exception!!)
            },
            confirmButton = {
                Button(
                    onClick = {
                        exception = null
                        loading = true
                        async { launch(lastTask!!) }
                    }
                ) {
                    Text(
                        text = i18n(R.string.try_again),
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDetails = !showDetails },
                ) {
                    Text(
                        text =
                            if (showDetails) i18n(R.string.hide_details)
                            else i18n(R.string.show_details),
                    )
                }
            }
        )
    }
}

@Composable
private fun ErrorDetails(
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
