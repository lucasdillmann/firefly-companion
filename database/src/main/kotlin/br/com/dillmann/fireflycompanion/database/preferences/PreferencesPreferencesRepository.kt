package br.com.dillmann.fireflycompanion.database.preferences

import android.content.Context
import androidx.core.content.edit
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.PreferencesRepository

internal class PreferencesPreferencesRepository(private val context: Context) : PreferencesRepository {
    companion object {
        private const val PREFERENCES_NAME = "main_preferences"
        private const val REQUIRE_BIOMETRIC_LOGIN_KEY = "require_biometric_login"
        private const val THEME_KEY = "theme"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun get(): Preferences? {
        val requireBiometricLogin = sharedPreferences.getBoolean(REQUIRE_BIOMETRIC_LOGIN_KEY, false)
        val theme = sharedPreferences
            .getString(THEME_KEY, Preferences.Theme.AUTO.name)
            ?.let(Preferences.Theme::valueOf)
            ?: Preferences.Theme.AUTO

        return Preferences(requireBiometricLogin, theme)
    }

    override suspend fun save(preferences: Preferences) {
        sharedPreferences.edit {
            putBoolean(REQUIRE_BIOMETRIC_LOGIN_KEY, preferences.requireBiometricLogin)
            putString(THEME_KEY, preferences.theme.name)
        }
    }
}
