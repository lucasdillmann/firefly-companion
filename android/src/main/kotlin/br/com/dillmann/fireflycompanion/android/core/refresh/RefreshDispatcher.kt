package br.com.dillmann.fireflycompanion.android.core.refresh

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

object RefreshDispatcher {
    private val listeners = mutableMapOf<RefreshListener, Any?>()

    @Synchronized
    fun subscribe(listener: RefreshListener, scope: Any?) {
        listeners += listener to scope
    }

    @Synchronized
    fun unsubscribe(listener: RefreshListener) {
        listeners -= listener
    }

    suspend fun notify(scope: Any? = null) {
        coroutineScope {
            listeners
                .filter {(_, supportedScope) -> scope == null || supportedScope == scope }
                .map { (handler, _) -> async { handler()} }
                .awaitAll()
        }
    }
}
