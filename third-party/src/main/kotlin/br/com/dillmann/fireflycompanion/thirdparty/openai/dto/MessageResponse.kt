package br.com.dillmann.fireflycompanion.thirdparty.openai.dto

import com.squareup.moshi.Json

internal data class MessageResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "output")
    val output: List<Output>,
) {
    data class Output(
        @Json(name = "type")
        val type: String,
        @Json(name = "content")
        val content: List<Content>? = null,
        @Json(name = "arguments")
        val arguments: String? = null,
        @Json(name = "name")
        val name: String? = null,
    )

    data class Content(
        @Json(name = "type")
        val type: String,
        @Json(name = "text")
        val text: String,
    )
}
