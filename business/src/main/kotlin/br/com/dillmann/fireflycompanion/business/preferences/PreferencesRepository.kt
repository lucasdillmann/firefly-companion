package br.com.dillmann.fireflycompanion.business.preferences

interface PreferencesRepository {
    suspend fun get(): Preferences?
    suspend fun save(preferences: Preferences)
}
