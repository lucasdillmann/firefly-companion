package br.com.dillmann.fireflycompanion.android.core.activity

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T> state(
    persistent: Boolean = true,
    loader: suspend () -> T,
): MutableState<T?> {
    val stateHolder =
        if (persistent) rememberSaveable { mutableStateOf<T?>(null) }
        else remember { mutableStateOf<T?>(null) }

    LaunchedEffect(Unit) {
        async { stateHolder.value = loader() }.join()
    }

    return stateHolder
}

@Composable
fun <T> state(
    value: T,
    persistent: Boolean = true,
): MutableState<T> =
    if (persistent) rememberSaveable { mutableStateOf(value) }
    else remember { mutableStateOf(value) }
