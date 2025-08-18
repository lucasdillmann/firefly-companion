package br.com.dillmann.fireflycompanion.business.assistant.model

data class LLMRequest(
    val model: String,
    val functions: List<Function>,
    val inputs: List<Input>,
    val instructions: String? = null,
    val previousResponseId: String? = null,
) {
    enum class Type {
        FUNCTION_CALL_OUTPUT,
        USER_PROMPT,
    }

    data class Function(
        val name: String,
        val description: String,
        val arguments: List<FunctionArgument>,
    )

    data class FunctionArgument(
        val name: String,
        val type: Any,
        val description: String,
        val required: Boolean,
    )

    data class Input(
        val type: Type,
        val content: String,
        val callId: String? = null,
    )
}
