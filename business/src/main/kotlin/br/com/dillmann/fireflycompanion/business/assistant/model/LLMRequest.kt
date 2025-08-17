package br.com.dillmann.fireflycompanion.business.assistant.model

data class LLMRequest(
    val model: String,
    val content: String,
    val type: Type,
    val functions: List<Function>,
    val instructions: String? = null,
    val previousId: String? = null,
    val callId: String? = null,
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
}
