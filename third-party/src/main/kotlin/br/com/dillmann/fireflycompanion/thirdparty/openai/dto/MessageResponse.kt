package br.com.dillmann.fireflycompanion.thirdparty.openai.dto

import com.google.gson.annotations.SerializedName

internal data class MessageResponse(
    val id: String,
    val status: String,
    val output: List<Output>?,
) {
    data class Output(
        val type: String,
        val content: List<Content>? = null,
        val arguments: String? = null,
        val name: String? = null,
        @SerializedName("call_id")
        val callId: String? = null,
    )

    data class Content(
        val type: String,
        val text: String,
    )
}
