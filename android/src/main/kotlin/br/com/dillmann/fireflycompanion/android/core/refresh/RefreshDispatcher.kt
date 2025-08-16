package br.com.dillmann.fireflycompanion.android.core.refresh

import br.com.dillmann.fireflycompanion.android.core.compose.async


object RefreshDispatcher {
    private val lockHolder = Any()
    private val listeners = mutableMapOf<RefreshListener, Any?>()

    @Synchronized
    fun subscribe(listener: RefreshListener, scope: Any?) {
        listeners += listener to scope
    }

    @Synchronized
    fun unsubscribe(listener: RefreshListener) {
        listeners -= listener
    }

    fun notify(scope: Any? = null) {
        synchronized(lockHolder) {
            async {
                listeners
                    .filter { (_, supportedScope) -> scope == null || supportedScope == scope }
                    .map { (handler, _) -> handler() }
            }.join()
        }
    }
}
