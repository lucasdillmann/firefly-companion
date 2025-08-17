package br.com.dillmann.fireflycompanion.thirdparty.openai.dto

import com.squareup.moshi.Json

internal data class MessageRequest(
    @Json(name = "model")
    val model: String,
    @Json(name = "input")
    val input: List<Input>,
    @Json(name = "tools")
    val tools: List<Tool> = emptyList(),
    @Json(name = "instructions")
    val instructions: String? = null,
    @Json(name = "format")
    val format: Format = Format("text"),
    @Json(name = "previous_response_id")
    val previousResponseId: String? = null,
    @Json(name = "temperature")
    val temperature: Double = 0.5,
    @Json(name = "background")
    val background: Boolean = false,
    @Json(name = "stream")
    val stream: Boolean = false,
) {
    data class Format(
        @Json(name = "type")
        val type: String,
        @Json(name = "value")
        val value: String? = null,
    )

    data class Input(
        @Json(name = "role")
        val role: String,
        @Json(name = "content")
        val content: List<InputContent>,
    )

    data class InputContent(
        @Json(name = "text")
        val text: String,
        @Json(name = "type")
        val type: String = "input_text",
    )

    data class Tool(
        @Json(name = "name")
        val name: String,
        @Json(name = "description")
        val description: String,
        @Json(name = "parameters")
        val parameters: ToolParameters = ToolParameters(),
        @Json(name = "type")
        val type: String = "function",
    )

    data class ToolParameters(
        @Json(name = "type")
        val type: String = "object",
        @Json(name = "properties")
        val properties: Map<String, ToolParameter> = emptyMap(),
        @Json(name = "required")
        val required: List<String> = emptyList(),
    )

    data class ToolParameter(
        @Json(name = "type")
        val type: String,
        @Json(name = "description")
        val description: String,
    )
}
