package br.com.dillmann.fireflycompanion.thirdparty.openai

import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMResponse
import br.com.dillmann.fireflycompanion.core.json.JsonConverter
import br.com.dillmann.fireflycompanion.core.json.parse
import br.com.dillmann.fireflycompanion.thirdparty.openai.dto.MessageRequest
import br.com.dillmann.fireflycompanion.thirdparty.openai.dto.MessageResponse
import br.com.dillmann.fireflycompanion.thirdparty.openai.dto.ModelResponse
import java.time.OffsetDateTime

internal class OpenAiConverter(
    private val jsonConverter: JsonConverter,
) {
    fun toDomain(input: ModelResponse) =
        input.data.map { it.id }

    fun toDomain(input: MessageResponse): LLMResponse {
        val messages = input.output.orEmpty().flatMap { output ->
            when (output.type) {
                "message" ->
                    output.content.orEmpty().map {
                        LLMResponse.SimpleText(
                            content = it.text,
                        )
                    }

                "function_call" -> {
                    val args =
                        runCatching { jsonConverter.parse<Map<String, Any?>>(output.arguments!!) }.getOrNull()

                    listOf(
                        LLMResponse.FunctionCall(
                            name = output.name!!,
                            callId = output.callId!!,
                            arguments = args,
                        ),
                    )
                }

                else -> emptyList()
            }
        }

        return LLMResponse(
            id = input.id,
            timestamp = OffsetDateTime.now(),
            messages = messages,
        )
    }

    fun toDto(request: LLMRequest): MessageRequest {
        val input = mutableListOf<MessageRequest.Input>()
        input +=
            when (request.type) {
                LLMRequest.Type.USER_PROMPT ->
                    MessageRequest.Input(
                        role = "user",
                        content = listOf(MessageRequest.InputContent(
                            type = "input_text",
                            text = request.content,
                        )),
                    )

                LLMRequest.Type.FUNCTION_CALL_OUTPUT ->
                    MessageRequest.Input(
                        type = "function_call_output",
                        callId = request.callId,
                        output = request.content,
                    )
            }

        return MessageRequest(
            model = request.model,
            instructions = request.instructions,
            previousResponseId = request.previousId,
            input = input,
            tools = request.functions.map { function ->
                MessageRequest.Tool(
                    name = function.name,
                    description = function.description,
                    parameters = MessageRequest.ToolParameters(
                        properties = function.arguments.associate { arg ->
                            arg.name to MessageRequest.ToolParameter(
                                type = arg.type,
                                description = arg.description
                            )
                        },
                        required = function.arguments.filter { it.required }.map { it.name }
                    )
                )
            },
        )
    }
}
