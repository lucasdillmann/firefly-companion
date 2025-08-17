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
        val messages = input.output.flatMap { output ->
            val results = mutableListOf<LLMResponse.Message>()

            output.content.orEmpty()
                .filter { it.type.lowercase() in listOf("output_text", "text") }
                .mapTo(results) {
                    LLMResponse.Message(
                        type = LLMResponse.Type.SIMPLE_TEXT,
                        content = LLMResponse.SimpleText(content = it.text)
                    )
                }

            output.content.orEmpty()
                .filter { it.type.lowercase() == "function_call" }
                .mapTo(results) {
                    val args =
                        runCatching { jsonConverter.parse<Map<String, Any?>>(output.arguments!!) }.getOrNull()

                    LLMResponse.Message(
                        type = LLMResponse.Type.FUNCTION_CALL,
                        content = LLMResponse.FunctionCall(
                            name = output.name!!,
                            arguments = args,
                        )
                    )
                }

            results
        }

        return LLMResponse(
            id = input.id,
            timestamp = OffsetDateTime.now(),
            messages = messages,
        )
    }

    fun toDto(request: LLMRequest) =
        MessageRequest(
            model = request.model,
            instructions = request.instructions,
            previousResponseId = request.previousResponseId,
            input = listOf(
                MessageRequest.Input(
                    role = "user",
                    content = listOf(MessageRequest.InputContent(text = request.prompt)),
                )
            ),
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
