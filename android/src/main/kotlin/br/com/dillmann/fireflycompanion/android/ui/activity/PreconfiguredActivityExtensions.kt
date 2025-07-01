package br.com.dillmann.fireflycompanion.android.ui.activity

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
