package br.com.dillmann.fireflycompanion.thirdparty.openai.dto


internal data class ModelResponse(
    val data: List<Model>,
) {
    data class Model(
        val id: String
    )
}
