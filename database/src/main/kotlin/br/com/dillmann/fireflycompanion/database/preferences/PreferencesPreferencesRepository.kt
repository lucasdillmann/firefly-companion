package br.com.dillmann.fireflycompanion.database.preferences

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.PreferencesRepository
import br.com.dillmann.fireflycompanion.core.json.JsonConverter
import br.com.dillmann.fireflycompanion.core.json.parse
import br.com.dillmann.fireflycompanion.database.context.ContextProvider

internal class PreferencesPreferencesRepository(
    private val contextProvider: ContextProvider,
    private val converter: JsonConverter,
) : PreferencesRepository {
    companion object {
        private const val PREFERENCES_NAME = "preferences"
        private const val VALUE_FIELD = "preferences"
    }

    override suspend fun get(): Preferences =
        try {
            preferences().getString(VALUE_FIELD, null)?.let(converter::parse) ?: Preferences()
        } catch (ex: Exception) {
            Log.w("PreferencesPreferencesRepository", "Unable to load preferences", ex)
            Preferences()
        }

    override suspend fun save(preferences: Preferences) {
        preferences().edit {
            putString(VALUE_FIELD, converter.toJson(preferences))
        }
    }

    private suspend fun preferences() =
        contextProvider.resolve().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}
