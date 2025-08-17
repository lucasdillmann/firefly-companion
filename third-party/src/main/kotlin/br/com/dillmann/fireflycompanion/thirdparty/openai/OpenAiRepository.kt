package br.com.dillmann.fireflycompanion.thirdparty.openai

import br.com.dillmann.fireflycompanion.business.assistant.AssistantRepository
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMResponse
import br.com.dillmann.fireflycompanion.core.json.JsonConverter
import br.com.dillmann.fireflycompanion.core.json.parse
import br.com.dillmann.fireflycompanion.thirdparty.openai.dto.MessageResponse
import br.com.dillmann.fireflycompanion.thirdparty.openai.dto.ModelResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

internal class OpenAiRepository(
    private val jsonConverter: JsonConverter,
    private val apiConverter: OpenAiConverter,
    private val baseUrl: String,
    private val accessToken: String?,
) : AssistantRepository {
    private companion object {
        private const val CONNECT_TIMEOUT_SECONDS = 10L
        private const val READ_TIMEOUT_SECONDS = 600L
        private const val WRITE_TIMEOUT_SECONDS = 600L
    }

    private val delegate =
        OkHttpClient
            .Builder()
            .authenticator { _, response ->
                response
                    .request
                    .newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            }
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    override suspend fun getAvailableModels(): List<String> {
        val request = Request.Builder().get().url("$baseUrl/models").build()
        val response = delegate.newCall(request).execute()
        val payload = jsonConverter.parse<ModelResponse>(response.body.string())
        return apiConverter.toDomain(payload)
    }

    override suspend fun getResponse(request: LLMRequest): LLMResponse {
        val inputPayload = apiConverter.toDto(request)
        val body = jsonConverter.toJson(inputPayload)!!.toRequestBody()
        val request = Request.Builder().post(body).url("$baseUrl/responses").build()
        val response = delegate.newCall(request).execute()
        val outputPayload = jsonConverter.parse<MessageResponse>(response.body.string())
        return apiConverter.toDomain(outputPayload)
    }
}
