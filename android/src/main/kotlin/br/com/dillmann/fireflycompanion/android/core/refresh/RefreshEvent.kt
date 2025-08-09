package br.com.dillmann.fireflycompanion.android.core.refresh

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import br.com.dillmann.fireflycompanion.android.core.compose.OnLifecycleEvent

@Composable
fun OnRefreshEvent(handler: RefreshListener) {
    RefreshDispatcher.subscribe(handler)

    OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) {
        RefreshDispatcher.unsubscribe(handler)
    }
}
