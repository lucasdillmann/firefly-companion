package br.com.dillmann.fireflycompanion.thirdparty.openai.dto

import com.google.gson.annotations.SerializedName

internal data class MessageRequest(
    val model: String,
    val input: List<Input>,
    val tools: List<Tool> = emptyList(),
    val instructions: String? = null,
    @SerializedName("previous_response_id")
    val previousResponseId: String? = null,
    val temperature: Double? = null,
    val background: Boolean = false,
    val stream: Boolean = false,
) {
    data class Input(
        val role: String? = null,
        val content: List<InputContent>? = null,
        val type: String? = null,
        val output: String? = null,
        @SerializedName("call_id")
        val callId: String? = null,
    )

    data class InputContent(
        val type: String,
        val text: String? = null,
    )

    data class Tool(
        val name: String,
        val description: String,
        val parameters: ToolParameters = ToolParameters(),
        val type: String = "function",
    )

    data class ToolParameters(
        val type: String = "object",
        val properties: Map<String, ToolParameter> = emptyMap(),
        val required: List<String> = emptyList(),
    )

    data class ToolParameter(
        val type: Any,
        val description: String,
    )
}
