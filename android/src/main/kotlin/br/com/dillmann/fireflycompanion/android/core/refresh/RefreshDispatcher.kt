package br.com.dillmann.fireflycompanion.android.core.refresh

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object RefreshDispatcher {
    private val listeners = mutableMapOf<RefreshListener, Any?>()

    fun subscribe(listener: RefreshListener, scope: Any?) {
        listeners += listener to scope
    }

    fun unsubscribe(listener: RefreshListener) {
        listeners -= listener
    }

    suspend fun notify(scope: Any? = null) {
        coroutineScope {
            for ((handler, supportedScope) in listeners) {
                if (scope == null || supportedScope == null || scope == supportedScope)
                    async { handler() }
            }
        }
    }
}
