package br.com.dillmann.fireflycompanion.business.autocomplete

import br.com.dillmann.fireflycompanion.business.autocomplete.usecase.AutoCompleteUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AutoCompleteModule =
    module {
        single { AutoCompleteService(get()) } binds arrayOf(AutoCompleteUseCase::class)
    }
