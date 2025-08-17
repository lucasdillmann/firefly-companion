package br.com.dillmann.fireflycompanion.business.assistant.model

import java.time.OffsetDateTime

data class AssistantMessage(
    val timestamp: OffsetDateTime,
    val content: String,
)
