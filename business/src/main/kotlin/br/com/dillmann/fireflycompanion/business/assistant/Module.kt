package br.com.dillmann.fireflycompanion.business.assistant

import br.com.dillmann.fireflycompanion.business.assistant.functions.AccountsAssistantFunction
import br.com.dillmann.fireflycompanion.business.assistant.functions.AssistantFunction
import br.com.dillmann.fireflycompanion.business.assistant.functions.SummaryAssistantFunction
import br.com.dillmann.fireflycompanion.business.assistant.functions.TransactionsAssistantFunction
import br.com.dillmann.fireflycompanion.business.assistant.usecase.GetAvailableModelsUseCase
import br.com.dillmann.fireflycompanion.business.assistant.usecase.StartAssistantSessionUseCase
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AssistantModule =
    module {
        single { TransactionsAssistantFunction(get()) } bind AssistantFunction::class
        single { AccountsAssistantFunction(get()) } bind AssistantFunction::class
        single { SummaryAssistantFunction(get()) } bind AssistantFunction::class

        single {
            AssistantService(get(), get(), getAll(), get())
        } binds arrayOf(
            StartAssistantSessionUseCase::class,
            GetAvailableModelsUseCase::class,
        )
    }
