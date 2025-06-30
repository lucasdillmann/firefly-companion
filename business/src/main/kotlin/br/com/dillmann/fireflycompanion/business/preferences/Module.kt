package br.com.dillmann.fireflycompanion.business.preferences

import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val PreferencesModule =
    module {
        single { PreferencesService(get()) } binds arrayOf(
            GetPreferencesUseCase::class,
            SavePreferencesUseCase::class,
        )
    }
