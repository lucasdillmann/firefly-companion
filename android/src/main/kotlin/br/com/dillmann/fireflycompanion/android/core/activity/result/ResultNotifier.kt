package br.com.dillmann.fireflycompanion.android.core.activity.result

class ResultNotifier {
    private val subscribers = mutableSetOf<ResultSubscriber>()

    fun subscribe(subscriber: ResultSubscriber) {
        subscribers += subscriber
    }

    fun unsubscribe(subscriber: ResultSubscriber) {
        subscribers -= subscriber
    }

    suspend fun notify(requestCode: Int, resultCode: Int) {
        subscribers.forEach {
            it.handleResult(requestCode, resultCode)
        }
    }
}
