package br.com.dillmann.fireflycompanion.business.assistant.session

import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession
import java.time.OffsetDateTime

internal class UnconfiguredAssistantSession : AssistantSession {
    override suspend fun sendMessage(message: String): List<AssistantMessage> {
        val message = AssistantMessage(
            timestamp = OffsetDateTime.now(),
            sender = AssistantMessage.Sender.ASSISTANT,
            content = "Sorry, but I'm not configured yet. Please configure me in the app settings.",
        )

        return listOf(message)
    }
}
