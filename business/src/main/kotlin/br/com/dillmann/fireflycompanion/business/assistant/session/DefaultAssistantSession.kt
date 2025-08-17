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
    private val responseHandler: suspend (AssistantMessage) -> Unit,
) : AssistantSession {
    private var previousResponseId: String? = null

    override suspend fun sendMessage(message: String) {
        val request = LLMRequest(
            model = model,
            instructions = buildInstructions(),
            prompt = message,
            previousResponseId = previousResponseId,
            functions = emptyList(),
        )

        val response = repository.getResponse(request)
        previousResponseId = response.id

        response
            .messages
            .filter { it.type == LLMResponse.Type.SIMPLE_TEXT }
            .forEach {
                val content = it.content as LLMResponse.SimpleText
                val message = AssistantMessage(
                    timestamp = OffsetDateTime.now(),
                    content = content.content,
                )
                responseHandler(message)
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
            - Reply using a simple and direct language
            - Keep in the context/subject of personal finances. Politely refuse to reply if user changes the subject.
            - You can make financial suggestions and predictions, but always state that those aren't investment 
              advisories and that the user is fully responsible for their decisions.
        """.trimIndent()
}
