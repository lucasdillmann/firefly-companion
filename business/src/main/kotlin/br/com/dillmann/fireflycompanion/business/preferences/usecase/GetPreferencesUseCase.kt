package br.com.dillmann.fireflycompanion.business.preferences.usecase

import br.com.dillmann.fireflycompanion.business.preferences.Preferences

interface GetPreferencesUseCase {
    suspend fun getPreferences(): Preferences
}
