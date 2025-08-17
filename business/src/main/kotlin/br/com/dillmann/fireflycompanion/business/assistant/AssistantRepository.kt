package br.com.dillmann.fireflycompanion.business.assistant

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMResponse

interface AssistantRepository {
    suspend fun getAvailableModels(): List<String>
    suspend fun getResponse(request: LLMRequest): LLMResponse
}
