package br.com.dillmann.fireflycompanion.business.assistant.model

import java.time.OffsetDateTime

data class LLMResponse(
    val id: String,
    val timestamp: OffsetDateTime,
    val messages: List<Message>,
) {
    sealed interface Message

    data class FunctionCall(
        val name: String,
        val callId: String,
        val arguments: Map<String, Any?>? = null,
    ) : Message

    data class SimpleText(
        val content: String,
    ) : Message
}
