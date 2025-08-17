package br.com.dillmann.fireflycompanion.thirdparty.openai.dto

import com.squareup.moshi.Json

internal data class ModelResponse(
    @Json(name = "data")
    val data: List<Model>,
) {
    data class Model(
        @Json(name = "id")
        val id: String
    )
}
