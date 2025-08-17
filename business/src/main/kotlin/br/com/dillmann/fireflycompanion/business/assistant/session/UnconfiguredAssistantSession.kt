package br.com.dillmann.fireflycompanion.business.assistant.session

import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession
import java.time.OffsetDateTime

internal class UnconfiguredAssistantSession(
    private val responseHandler: suspend (AssistantMessage) -> Unit,
) : AssistantSession {
    override suspend fun sendMessage(message: String) {
        val message = AssistantMessage(
            timestamp = OffsetDateTime.now(),
            content = "Sorry, but I'm not configured yet. Please configure me in the app settings.",
        )

        responseHandler(message)
    }
}
