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
    private val useStatefulResponses: Boolean,
) : AssistantSession {
    private var previousResponseId: String? = null
    private val statelessTranscript = mutableListOf<LLMRequest.Input>()

    override suspend fun sendMessage(
        message: String,
        callback: Callback,
    ) {
        val userInput =
            LLMRequest.Input(
                type = LLMRequest.Type.USER_PROMPT,
                content = message,
            )
        if (useStatefulResponses) {
            runModelRound(listOf(userInput), callback)
        } else {
            statelessTranscript.add(userInput)
            runModelRound(null, callback)
        }
    }

    private suspend fun runModelRound(
        statefulInputDelta: List<LLMRequest.Input>?,
        callback: Callback,
    ) {
        callback.state(State.THINKING)

        val request =
            if (useStatefulResponses) {
                val inputs = requireNotNull(statefulInputDelta) { "stateful round requires request inputs" }
                baseRequest().copy(inputs = inputs)
            } else {
                require(statefulInputDelta == null) { "stateless round builds inputs from the transcript" }
                baseRequest().copy(
                    previousResponseId = null,
                    inputs = statelessTranscript.toList(),
                )
            }

        val response = repository.getResponse(request)
        if (useStatefulResponses) {
            previousResponseId = response.id
        }

        if (response.messages.isEmpty()) {
            callback.state(State.IDLE)
            return
        }

        if (!useStatefulResponses) {
            for (message in response.messages) {
                when (message) {
                    is LLMResponse.SimpleText ->
                        statelessTranscript.add(
                            LLMRequest.Input(
                                type = LLMRequest.Type.ASSISTANT_TEXT,
                                content = message.content,
                            )
                        )

                    is LLMResponse.FunctionCall -> {
                        val arguments =
                            message.arguments?.let { converter.toJson(it) } ?: "{}"
                        statelessTranscript.add(
                            LLMRequest.Input(
                                type = LLMRequest.Type.ASSISTANT_FUNCTION_CALL,
                                content = arguments,
                                callId = message.callId,
                                name = message.name,
                            )
                        )
                    }
                }
            }
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
            if (useStatefulResponses) {
                runModelRound(functionOutputs, callback)
            } else {
                for (output in functionOutputs) {
                    statelessTranscript.add(output)
                }
                runModelRound(null, callback)
            }
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
            previousResponseId = if (useStatefulResponses) previousResponseId else null,
            functions = functions.map(AssistantFunction::metadata),
            inputs = emptyList(),
        )

    private fun buildInstructions() =
        """
            Guidelines:
            - You're a personal finances assistant for the Firefly Companion app.
            - Now is ${OffsetDateTime.now()} and the user language is $userLanguage.
            - Keep responses concise, non-technical and only with details that add value.
            - Keep in the subject of personal finances. Politely refuse to change the subject.
            - You're in a text chat interface. Always generate responses in plain text.
            - When you're about to call a function, send another message acknowledging the user and telling
              that you'll get more data to be ably to reply to him. Super-important: Don't forget to send the message 
              with the function call.
            - Any data provided came from a Firefly III server. Use Firefly III concepts and terminology when working
              with the data.
            - Always present dates in the official format for the $userLanguage language.
        """.trimIndent()
}
