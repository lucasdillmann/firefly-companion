package br.com.dillmann.fireflycompanion.business.assistant.functions

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest

internal interface AssistantFunction {
    fun metadata(): LLMRequest.Function
    suspend fun execute(arguments: Map<String, Any?>?): Any?
}
