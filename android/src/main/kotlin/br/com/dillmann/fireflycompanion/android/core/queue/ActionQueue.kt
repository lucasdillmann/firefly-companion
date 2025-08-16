package br.com.dillmann.fireflycompanion.android.core.queue

import br.com.dillmann.fireflycompanion.android.core.compose.async
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import java.io.Serializable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val executors =
    CacheBuilder
        .newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .removalListener<ActionQueue, ExecutorService> {
            it.value?.shutdown()
        }
        .build(
            object : CacheLoader<ActionQueue, ExecutorService>() {
                override fun load(key: ActionQueue) = Executors.newSingleThreadExecutor()
            }
        )

class ActionQueue : Serializable {
    fun add(action: suspend () -> Unit) {
        executors
            .get(this)
            .execute {
                async { action() }.join()
            }
    }
}
