package br.com.dillmann.fireflycompanion.business.assistant

interface AssistantSession {
    suspend fun sendMessage(message: String)
}
