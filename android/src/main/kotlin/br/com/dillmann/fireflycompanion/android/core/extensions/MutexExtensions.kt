package br.com.dillmann.fireflycompanion.android.core.extensions

import br.com.dillmann.fireflycompanion.android.core.compose.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun Mutex.asyncLock(action: suspend () -> Unit) {
    async {
        withLock(owner = this) { action() }
    }
}
