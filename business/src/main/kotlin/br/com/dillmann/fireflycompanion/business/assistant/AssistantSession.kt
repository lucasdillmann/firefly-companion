package br.com.dillmann.fireflycompanion.business.assistant

import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage

interface AssistantSession {
    enum class State {
        IDLE,
        THINKING,
        GATHERING_DATA,
    }

    interface Callback {
        suspend fun message(response: AssistantMessage)
        suspend fun state(state: State)
    }

    suspend fun sendMessage(message: String, callback: Callback)
}
