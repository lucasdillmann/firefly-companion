package br.com.dillmann.fireflycompanion.business.assistant.model

import java.time.OffsetDateTime

data class LLMResponse(
    val id: String,
    val timestamp: OffsetDateTime,
    val messages: List<Message>,
) {
    enum class Type {
        SIMPLE_TEXT,
        FUNCTION_CALL,
    }

    data class Message(
        val type: Type,
        val content: Any?,
    )

    data class FunctionCall(
        val name: String,
        val arguments: Map<String, Any?>? = null,
    )

    data class SimpleText(
        val content: String,
    )
}
