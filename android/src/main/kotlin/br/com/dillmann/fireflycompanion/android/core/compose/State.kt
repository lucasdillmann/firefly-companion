package br.com.dillmann.fireflycompanion.android.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import br.com.dillmann.fireflycompanion.android.core.components.action.LoadErrorDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface PersistentState<out T> {
    data object Loading : PersistentState<Nothing>

    data class Ready<T>(val value: T) : PersistentState<T>

    class Failed(val exception: Exception) : PersistentState<Nothing>

    data object Dismissed : PersistentState<Nothing>
}

@Composable
fun <T> persistent(loader: suspend () -> T): MutableState<PersistentState<T>> {
    val stateHolder = remember { mutableStateOf<PersistentState<T>>(PersistentState.Loading) }
    var retryEpoch by remember { mutableIntStateOf(0) }

    LaunchedEffect(retryEpoch) {
        when (stateHolder.value) {
            is PersistentState.Ready -> return@LaunchedEffect
            PersistentState.Dismissed -> return@LaunchedEffect
            else -> Unit
        }

        stateHolder.value = PersistentState.Loading
        try {
            val result = withContext(Dispatchers.IO) { loader() }
            stateHolder.value = PersistentState.Ready(result)
        } catch (exception: Exception) {
            stateHolder.value = PersistentState.Failed(exception)
        }
    }

    if (stateHolder.value is PersistentState.Failed) {
        val failed = stateHolder.value as PersistentState.Failed
        LoadErrorDialog(
            exception = failed.exception,
            onDismiss = { stateHolder.value = PersistentState.Dismissed },
            onRetry = { retryEpoch++ },
        )
    }

    return stateHolder
}

@Composable
fun <T> persistent(value: T): MutableState<T> =
    rememberSaveable { mutableStateOf(value) }

@Composable
fun <T> volatile(value: T): MutableState<T> =
    remember { mutableStateOf(value) }

@Composable
fun <T> volatile(loader: suspend () -> T): MutableState<T> =
    remember {
        val value = async { loader() }.get()
        mutableStateOf(value)
    }

@Composable
fun <T> emptyVolatile(): MutableState<T?> =
    volatile(null)
