package br.com.dillmann.fireflycompanion.business.assistant

import br.com.dillmann.fireflycompanion.business.assistant.usecase.GetAvailableModelsUseCase
import br.com.dillmann.fireflycompanion.business.assistant.usecase.StartAssistantSessionUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AssistantModule =
    module {
        single { AssistantService(get(), get()) } binds arrayOf(
            StartAssistantSessionUseCase::class,
            GetAvailableModelsUseCase::class,
        )
    }
