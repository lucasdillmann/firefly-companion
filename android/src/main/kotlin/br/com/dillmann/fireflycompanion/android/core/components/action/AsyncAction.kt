package br.com.dillmann.fireflycompanion.android.core.components.action

import androidx.compose.foundation.layout.*
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
        LoadErrorDialog(
            exception = exception!!,
            onDismiss = { exception = null },
            onRetry = {
                exception = null
                loading = true
                async { launch(lastTask!!) }
            },
        )
    }
}
