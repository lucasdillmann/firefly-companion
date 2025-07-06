package br.com.dillmann.fireflycompanion.database.context

import android.content.Context

interface ContextProvider {
    suspend fun resolve(): Context
}
