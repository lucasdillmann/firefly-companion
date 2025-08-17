package br.com.dillmann.fireflycompanion.thirdparty.openai

import br.com.dillmann.fireflycompanion.business.assistant.AssistantRepository
import br.com.dillmann.fireflycompanion.business.assistant.AssistantRepositoryProvider
import br.com.dillmann.fireflycompanion.core.json.JsonConverter

internal class OpenAiRepositoryProvider(
    private val apiConverter: OpenAiConverter,
    private val jsonConverter: JsonConverter,
) : AssistantRepositoryProvider {
    override suspend fun provide(baseUrl: String, accessToken: String?): AssistantRepository =
        OpenAiRepository(
            jsonConverter = jsonConverter,
            apiConverter = apiConverter,
            baseUrl = baseUrl,
            accessToken = accessToken,
        )
}
