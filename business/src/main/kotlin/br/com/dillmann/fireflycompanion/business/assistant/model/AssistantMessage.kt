package br.com.dillmann.fireflycompanion.business.assistant.model

import java.io.Serializable
import java.time.OffsetDateTime

data class AssistantMessage(
    val timestamp: OffsetDateTime,
    val sender: Sender,
    val content: String,
) : Serializable {
    enum class Sender {
        USER,
        ASSISTANT
    }
}
