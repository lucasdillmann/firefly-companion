package br.com.dillmann.fireflycompanion.android.core.activity

import androidx.compose.runtime.*

@Composable
fun <T> state(loader: suspend () -> T): MutableState<T?> {
    val stateHolder = remember { mutableStateOf<T?>(null) }

    LaunchedEffect(loader.toString()) {
        async { stateHolder.value = loader() }
    }

    return stateHolder
}

@Composable
fun <T> state(value: T): MutableState<T> =
    remember { mutableStateOf(value) }
