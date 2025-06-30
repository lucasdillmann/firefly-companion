package br.com.dillmann.fireflycompanion.business.preferences

import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase

internal class PreferencesService(
    private val repository: PreferencesRepository,
) : GetPreferencesUseCase, SavePreferencesUseCase {
    override suspend fun getPreferences(): Preferences =
        repository.get() ?: Preferences()

    override suspend fun savePreferences(preferences: Preferences) =
        repository.save(preferences)
}
