package br.com.dillmann.fireflycompanion.android.core.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture

fun <T> PreconfiguredActivity.async(action: suspend () -> T): CompletableFuture<T> =
    lifecycleScope.future {
        withContext(Dispatchers.IO) {
            action()
        }
    }

@Composable
fun <T> PreconfiguredActivity.state(loader: suspend () -> T): MutableState<T?> {
    val stateHolder = remember { mutableStateOf<T?>(null) }

    LaunchedEffect(loader.toString()) {
        async { stateHolder.value = loader() }
    }

    return stateHolder
}

@Composable
fun <T> PreconfiguredActivity.state(value: T): MutableState<T> =
    remember { mutableStateOf(value) }
