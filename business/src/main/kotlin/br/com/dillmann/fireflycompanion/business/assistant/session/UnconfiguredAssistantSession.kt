package br.com.dillmann.fireflycompanion.business.assistant.session

import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession.State
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession.Callback
import java.time.OffsetDateTime

internal class UnconfiguredAssistantSession : AssistantSession {
    override suspend fun sendMessage(
        message: String,
        callback: Callback,
    ) {
        callback.state(State.THINKING)

        val message = AssistantMessage(
            timestamp = OffsetDateTime.now(),
            sender = AssistantMessage.Sender.ASSISTANT,
            content = "Sorry, but I'm not configured yet. Please configure me in the app settings.",
        )

        callback.message(message)
        callback.state(State.IDLE)
    }
}
