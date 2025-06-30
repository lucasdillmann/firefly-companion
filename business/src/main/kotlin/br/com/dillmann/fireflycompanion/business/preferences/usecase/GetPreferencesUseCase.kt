package br.com.dillmann.fireflycompanion.business.preferences.usecase

import br.com.dillmann.fireflycompanion.business.preferences.Preferences

fun interface GetPreferencesUseCase {
    suspend fun getPreferences(): Preferences
}
