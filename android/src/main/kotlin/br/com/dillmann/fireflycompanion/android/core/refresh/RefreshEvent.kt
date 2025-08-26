package br.com.dillmann.fireflycompanion.android.core.refresh

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import br.com.dillmann.fireflycompanion.android.core.compose.OnLifecycleEvent

@Composable
fun OnRefreshEvent(id: String, scope: Any? = null, handler: RefreshListener) {
    OnLifecycleEvent(Lifecycle.Event.ON_CREATE) {
        RefreshDispatcher.subscribe(id, handler, scope)
    }

    OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) {
        RefreshDispatcher.unsubscribe(id)
    }
}
