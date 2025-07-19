package br.com.dillmann.fireflycompanion.android.core.activity.result

fun interface ResultSubscriber {
    suspend fun handleResult(requestCode: Int, resultCode: Int)
}
