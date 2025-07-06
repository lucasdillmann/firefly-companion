package br.com.dillmann.fireflycompanion.android.core.context

import android.annotation.SuppressLint
import android.content.Context
import android.os.LocaleList
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.database.context.ContextProvider
import java.util.*

@SuppressLint("StaticFieldLeak")
object AppContext : ContextProvider {
    private lateinit var root: Context
    private lateinit var current: Context

    override suspend fun resolve(): Context =
        current

    fun init(context: Context) {
        if (::root.isInitialized)
            return

        root = context
        current = context
    }

    fun reconfigure(preferences: Preferences) {
        current = when (preferences.language) {
            Preferences.Language.AUTO -> root
            Preferences.Language.ENGLISH -> buildContext("en")
            Preferences.Language.PORTUGUESE -> buildContext("pt")
        }
    }

    private fun buildContext(languageId: String): Context {
        val configuration = root.resources.configuration
        val locale = Locale(languageId)
        val localeList = LocaleList(locale)
        configuration.setLocales(localeList)

        return root.createConfigurationContext(configuration)
    }
}
