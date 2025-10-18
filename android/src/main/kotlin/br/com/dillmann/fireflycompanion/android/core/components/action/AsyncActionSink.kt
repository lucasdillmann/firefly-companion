package br.com.dillmann.fireflycompanion.android.core.components.action

class AsyncActionSink {
    private val queue = mutableListOf<AsyncActionTask>()
    private lateinit var delegate: (AsyncActionTask) -> Unit

    fun push(task: AsyncActionTask) {
        if (!::delegate.isInitialized)
            queue += task
        else
            delegate(task)
    }

    fun attach(executor: (AsyncActionTask) -> Unit) {
        delegate = executor

        queue.forEach(executor)
        queue.clear()
    }

    fun detach() {
        delegate = {}
    }
}
