package br.com.dillmann.fireflycompanion.android.core.queue

import br.com.dillmann.fireflycompanion.android.core.compose.async
import java.util.concurrent.Executors

class ActionQueue {
    private val queue = Executors.newSingleThreadExecutor()

    fun add(action: suspend () -> Unit) {
        queue.execute {
            async { action() }.join()
        }
    }
}
