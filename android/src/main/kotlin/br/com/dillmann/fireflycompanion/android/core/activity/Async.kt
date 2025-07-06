package br.com.dillmann.fireflycompanion.android.core.activity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

fun <T> async(action: suspend () -> T): CompletableFuture<T> =
    CoroutineScope(Dispatchers.IO + Job()).future { action() }
