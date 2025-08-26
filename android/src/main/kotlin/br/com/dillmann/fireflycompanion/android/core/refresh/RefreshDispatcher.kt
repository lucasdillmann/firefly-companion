package br.com.dillmann.fireflycompanion.android.core.refresh

import br.com.dillmann.fireflycompanion.android.core.compose.async

object RefreshDispatcher {
    private data class Item(val listener: RefreshListener, val scope: Any?)
    private val listeners = mutableMapOf<String, Item>()

    @Synchronized
    fun subscribe(qualifier: String, listener: RefreshListener, scope: Any?) {
        listeners += qualifier to Item(listener, scope)
    }

    @Synchronized
    fun unsubscribe(id: String) {
        listeners -= id
    }

    fun notify(scope: Any? = null) {
        listeners
            .values
            .filter { scope == null || it.scope == scope }
            .map { async { it.listener() } }
    }
}
