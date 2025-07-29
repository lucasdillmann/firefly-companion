package br.com.dillmann.fireflycompanion.android.core.refresh

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object RefreshDispatcher {
    private val listeners = mutableSetOf<RefreshListener>()

    fun subscribe(listener: RefreshListener) {
        listeners += listener
    }

    fun unsubscribe(listener: RefreshListener) {
        listeners -= listener
    }

    suspend fun notify() {
        coroutineScope {
            for (listener in listeners) {
                async { listener() }
            }
        }
    }
}
