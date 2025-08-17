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
        val request = baseRequest().copy(
            type = LLMRequest.Type.USER_PROMPT,
            content = message,
        )

        executeRequest(request, callback)
    }

    private suspend fun executeRequest(request: LLMRequest, callback: Callback) {
        callback.state(State.THINKING)

        val response = repository.getResponse(request)
        previousResponseId = response.id

        if (response.messages.isEmpty()) {
            callback.state(State.IDLE)
            return
        }

        response
            .messages
            .forEach { message ->
                when (message) {
                    is LLMResponse.SimpleText -> handleSimpleMessage(message, callback)
                    is LLMResponse.FunctionCall -> handleFunctionCall(message, callback)
                }
            }
    }

    private suspend fun handleFunctionCall(
        content: LLMResponse.FunctionCall,
        callback: Callback,
    ) {
        callback.state(State.GATHERING_DATA)
        val function = functions.firstOrNull { it.metadata().name == content.name }
        if (function == null) {
            callback.state(State.IDLE)
            return
        }

        val functionOutcome = function.execute(content.arguments)?.let(converter::toJson) ?: "null"
        val request = baseRequest().copy(
            type = LLMRequest.Type.FUNCTION_CALL_OUTPUT,
            callId = content.callId,
            content = functionOutcome,
        )

        executeRequest(request, callback)
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
            type = LLMRequest.Type.USER_PROMPT,
            model = model,
            instructions = buildInstructions(),
            content = "",
            previousId = previousResponseId,
            functions = functions.map(AssistantFunction::metadata),
        )

    private fun buildInstructions() =
        """
            You're a helpful personal finances assistant running inside an app called Firefly Companion.
            Today is ${OffsetDateTime.now()} and the user language is $userLanguage.
            You must always (with no exceptions):
            - Keep responses concise with all important details
            - Be polite and friendly
            - Keep in the subject of personal finances. Politely refuse to reply if user changes the subject.
            - You're in a simple text chat interface. Do not offer anything that can't be done in a text chat (like
              exporting files) and generate responses in plain text (no markdown or alike), but you can use emojis.
        """.trimIndent()
}
