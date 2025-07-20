package br.com.dillmann.fireflycompanion.android.core.extensions

import java.util.concurrent.CompletableFuture

fun CompletableFuture<out Any>?.done() =
    this == null || isDone

fun CompletableFuture<out Any>?.cancel() =
    this?.cancel(true)
