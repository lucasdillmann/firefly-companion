package br.com.dillmann.fireflycompanion.business.preferences.usecase

import br.com.dillmann.fireflycompanion.business.preferences.Preferences

fun interface SavePreferencesUseCase {
    suspend fun savePreferences(preferences: Preferences)
}
