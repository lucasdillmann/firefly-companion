package br.com.dillmann.fireflycompanion.android.core.refresh

object RefreshDispatcher {
    private val listeners = mutableMapOf<RefreshListener, Any?>()

    fun subscribe(listener: RefreshListener, scope: Any?) {
        listeners += listener to scope
    }

    fun unsubscribe(listener: RefreshListener) {
        listeners -= listener
    }

    suspend fun notify(scope: Any? = null) {
        for ((handler, supportedScope) in listeners) {
            if (scope == null || supportedScope == null || scope == supportedScope)
                handler()
        }
    }
}
