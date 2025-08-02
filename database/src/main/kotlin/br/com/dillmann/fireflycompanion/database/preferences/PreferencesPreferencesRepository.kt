package br.com.dillmann.fireflycompanion.database.preferences

import android.content.Context
import androidx.core.content.edit
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.PreferencesRepository
import br.com.dillmann.fireflycompanion.database.context.ContextProvider

internal class PreferencesPreferencesRepository(private val contextProvider: ContextProvider) : PreferencesRepository {
    companion object {
        private const val PREFERENCES_NAME = "main_preferences"
        private const val REQUIRE_BIOMETRIC_LOGIN_KEY = "require_biometric_login"
        private const val THEME_KEY = "theme"
        private const val LANGUAGE_KEY = "language"
        private const val LOCK_TIMEOUT = "lock_timeout"
    }

    override suspend fun get(): Preferences? {
        val sharedPreferences = resolvePreferences()
        val requireBiometricLogin = sharedPreferences
            .getBoolean(REQUIRE_BIOMETRIC_LOGIN_KEY, false)
        val theme = sharedPreferences
            .getString(THEME_KEY, Preferences.Theme.AUTO.name)
            ?.let(Preferences.Theme::valueOf)
            ?: Preferences.Theme.AUTO
        val language = sharedPreferences
            .getString(LANGUAGE_KEY, Preferences.Language.AUTO.name)
            ?.let(Preferences.Language::valueOf)
            ?: Preferences.Language.AUTO
        val lockTimeout = sharedPreferences
            .getString(LOCK_TIMEOUT, Preferences.LockTimeout.IMMEDIATELY.name)
            ?.let(Preferences.LockTimeout::valueOf)
            ?: Preferences.LockTimeout.IMMEDIATELY

        return Preferences(requireBiometricLogin, theme, language, lockTimeout)
    }

    override suspend fun save(preferences: Preferences) {
        resolvePreferences().edit {
            putBoolean(REQUIRE_BIOMETRIC_LOGIN_KEY, preferences.requireBiometricLogin)
            putString(THEME_KEY, preferences.theme.name)
            putString(LANGUAGE_KEY, preferences.language.name)
            putString(LOCK_TIMEOUT, preferences.lockTimeout.name)
        }
    }

    private suspend fun resolvePreferences() =
        contextProvider.resolve().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}
