package br.com.dillmann.fireflycompanion.android.core.activity

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T> persistent(loader: suspend () -> T, ): MutableState<T?> {
    val stateHolder = rememberSaveable { mutableStateOf<T?>(null) }

    LaunchedEffect(Unit) {
        async {
            if (stateHolder.value == null)
                stateHolder.value = loader()
        }
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
fun <T> emptyVolatile(): MutableState<T?> =
    volatile(null)
