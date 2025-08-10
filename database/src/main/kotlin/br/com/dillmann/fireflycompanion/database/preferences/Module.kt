package br.com.dillmann.fireflycompanion.database.preferences

import br.com.dillmann.fireflycompanion.business.preferences.PreferencesRepository
import org.koin.dsl.module

internal val PreferencesModule =
    module {
        single<PreferencesRepository> { PreferencesPreferencesRepository(get(), get()) }
    }
