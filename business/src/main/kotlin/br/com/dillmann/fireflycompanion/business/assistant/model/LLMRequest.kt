package br.com.dillmann.fireflycompanion.business.assistant.model

data class LLMRequest(
    val model: String,
    val prompt: String,
    val instructions: String?,
    val previousResponseId: String?,
    val functions: List<Function>,
) {
    data class Function(
        val name: String,
        val description: String,
        val arguments: List<FunctionArgument>,
    )

    data class FunctionArgument(
        val name: String,
        val type: String,
        val description: String,
        val required: Boolean,
    )
}
