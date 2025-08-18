package br.com.dillmann.fireflycompanion.business.assistant.session

import br.com.dillmann.fireflycompanion.business.assistant.AssistantRepository
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession.Callback
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession.State
import br.com.dillmann.fireflycompanion.business.assistant.functions.AssistantFunction
import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMResponse
import br.com.dillmann.fireflycompanion.core.json.JsonConverter
import java.time.OffsetDateTime

internal class DefaultAssistantSession(
    private val repository: AssistantRepository,
    private val model: String,
    private val userLanguage: String,
    private val functions: List<AssistantFunction>,
    private val converter: JsonConverter,
) : AssistantSession {
    private var previousResponseId: String? = null

    override suspend fun sendMessage(
        message: String,
        callback: Callback,
    ) {
        val inputs = listOf(
            LLMRequest.Input(
                type = LLMRequest.Type.USER_PROMPT,
                content = message,
            ),
        )

        executeRequest(inputs, callback)
    }

    private suspend fun executeRequest(inputs: List<LLMRequest.Input>, callback: Callback) {
        callback.state(State.THINKING)

        val request = baseRequest().copy(inputs = inputs)
        val response = repository.getResponse(request)
        previousResponseId = response.id

        if (response.messages.isEmpty()) {
            callback.state(State.IDLE)
            return
        }

        response
            .messages
            .filterIsInstance<LLMResponse.SimpleText>()
            .forEach { handleSimpleMessage(it, callback) }

        val functionOutputs =
            response
                .messages
                .filterIsInstance<LLMResponse.FunctionCall>()
                .mapNotNull { handleFunctionCall(it, callback) }

        if (functionOutputs.isNotEmpty()) {
            executeRequest(functionOutputs, callback)
        }
    }

    private suspend fun handleFunctionCall(
        content: LLMResponse.FunctionCall,
        callback: Callback,
    ) : LLMRequest.Input? {
        callback.state(State.GATHERING_DATA)

        val function = functions.firstOrNull { it.metadata().name == content.name }
        if (function == null) {
            callback.state(State.IDLE)
            return null
        }

        val functionOutcome = function.execute(content.arguments)?.let(converter::toJson) ?: "null"
        return LLMRequest.Input(
            type = LLMRequest.Type.FUNCTION_CALL_OUTPUT,
            callId = content.callId,
            content = functionOutcome,
        )
    }

    private suspend fun handleSimpleMessage(
        content: LLMResponse.SimpleText,
        callback: Callback,
    ) {
        val message = AssistantMessage(
            timestamp = OffsetDateTime.now(),
            sender = AssistantMessage.Sender.ASSISTANT,
            content = content.content,
        )

        callback.message(message)
        callback.state(State.IDLE)
    }

    private fun baseRequest() =
        LLMRequest(
            model = model,
            instructions = buildInstructions(),
            previousResponseId = previousResponseId,
            functions = functions.map(AssistantFunction::metadata),
            inputs = emptyList(),
        )

    private fun buildInstructions() =
        """
            You're a personal finances assistant running inside an app called Firefly Companion.
            Today is ${OffsetDateTime.now()} and the user language is $userLanguage.
            You must always:
            - Keep responses concise with all important details
            - Be polite and professional
            - Keep in the subject of personal finances. Politely refuse to change the subject.
            - You're in a simple text chat interface. Do not offer anything that can't be done in a text chat (like
              exporting files). Always generate responses in plain text (no markdown or alike), but you can use emojis.
            - When you're about to call a function, send another message describing what information you're about 
              to get and why you need it. Super-important: Don't forget to send the message with the function call.
        """.trimIndent()
}
