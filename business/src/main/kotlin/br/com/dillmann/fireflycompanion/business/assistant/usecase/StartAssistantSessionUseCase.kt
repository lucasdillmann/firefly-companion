package br.com.dillmann.fireflycompanion.business.assistant.usecase

import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession

interface StartAssistantSessionUseCase {
    suspend fun startSession(
        userLanguage: String,
        responseHandler: suspend (AssistantMessage) -> Unit,
    ): AssistantSession
}
