package br.com.dillmann.fireflycompanion.android.core.i18n

import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.database.context.ContextProvider
import kotlinx.coroutines.runBlocking

fun i18n(key: Int): String =
    runBlocking { get<ContextProvider>().resolve() }.getString(key)
