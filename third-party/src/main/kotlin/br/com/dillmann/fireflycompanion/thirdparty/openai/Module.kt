package br.com.dillmann.fireflycompanion.thirdparty.openai

import br.com.dillmann.fireflycompanion.business.assistant.AssistantRepositoryProvider
import org.koin.dsl.module

internal val OpenAiModule =
    module {
        single { OpenAiConverter(get()) }
        single<AssistantRepositoryProvider> { OpenAiRepositoryProvider(get(), get()) }
    }
