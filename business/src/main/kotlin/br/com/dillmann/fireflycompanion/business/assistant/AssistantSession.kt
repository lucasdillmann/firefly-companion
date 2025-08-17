package br.com.dillmann.fireflycompanion.business.assistant

import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage

interface AssistantSession {
    suspend fun sendMessage(message: String): List<AssistantMessage>
}
