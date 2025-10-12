package br.com.dillmann.fireflycompanion.android.core.koin

inline fun <reified T: Any> get() =
    KoinManager.koin().get<T>()
