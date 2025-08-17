package br.com.dillmann.fireflycompanion.business.assistant.session

import br.com.dillmann.fireflycompanion.business.assistant.AssistantRepository
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession
import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMRequest
import br.com.dillmann.fireflycompanion.business.assistant.model.LLMResponse
import java.time.OffsetDateTime

internal class DefaultAssistantSession(
    private val repository: AssistantRepository,
    private val model: String,
    private val userLanguage: String,
) : AssistantSession {
    private var previousResponseId: String? = null

    override suspend fun sendMessage(message: String): List<AssistantMessage> {
        val request = LLMRequest(
            model = model,
            instructions = buildInstructions(),
            prompt = message,
            previousResponseId = previousResponseId,
            functions = emptyList(),
        )

        val response = repository.getResponse(request)
        previousResponseId = response.id

        return response
            .messages
            .filter { it.type == LLMResponse.Type.SIMPLE_TEXT }
            .map {
                val content = it.content as LLMResponse.SimpleText
                AssistantMessage(
                    timestamp = OffsetDateTime.now(),
                    sender = AssistantMessage.Sender.ASSISTANT,
                    content = content.content,
                )
            }
    }

    private fun buildInstructions() =
        """
            You're a helpful personal finances assistant running inside an app called Firefly Companion.
            Today is ${OffsetDateTime.now()}
            You must always (with no exceptions):
            - Reply in the user's language, $userLanguage
            - Keep the responses concise but with all the important details
            - Be polite and friendly
            - Keep in the context/subject of personal finances. Politely refuse to reply if user changes the subject.
        """.trimIndent()
}
