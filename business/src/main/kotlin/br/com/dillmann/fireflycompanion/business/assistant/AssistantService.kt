package br.com.dillmann.fireflycompanion.business.assistant

import br.com.dillmann.fireflycompanion.business.assistant.functions.AssistantFunction
import br.com.dillmann.fireflycompanion.business.assistant.session.DefaultAssistantSession
import br.com.dillmann.fireflycompanion.business.assistant.session.UnconfiguredAssistantSession
import br.com.dillmann.fireflycompanion.business.assistant.usecase.GetAvailableModelsUseCase
import br.com.dillmann.fireflycompanion.business.assistant.usecase.StartAssistantSessionUseCase
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.core.json.JsonConverter

internal class AssistantService(
    private val repositoryProvider: AssistantRepositoryProvider,
    private val preferencesUseCase: GetPreferencesUseCase,
    private val functions: List<AssistantFunction>,
    private val converter: JsonConverter,
) : StartAssistantSessionUseCase, GetAvailableModelsUseCase {
    override suspend fun startSession(
        userLanguage: String,
    ): AssistantSession {
        val preferences = preferencesUseCase.getPreferences().assistant
        if (preferences.provider == Preferences.AssistantProvider.DISABLED)
            return UnconfiguredAssistantSession()

        return DefaultAssistantSession(
            repository = resolveRepository(preferences),
            userLanguage = userLanguage,
            model = preferences.model!!,
            functions = functions,
            converter = converter,
        )
    }

    override suspend fun getAvailableModels(): List<String> {
        val preferences = preferencesUseCase.getPreferences().assistant
        if (preferences.provider == Preferences.AssistantProvider.DISABLED)
            return emptyList()

        return resolveRepository(preferences).getAvailableModels().sortedBy { it }
    }

    private suspend fun resolveRepository(preferences: Preferences.Assistant): AssistantRepository {
        val baseUrl =
            if (preferences.provider == Preferences.AssistantProvider.OPEN_AI) "https://api.openai.com/v1"
            else preferences.url!!

        return repositoryProvider.provide(baseUrl, preferences.accessToken)
    }
}
