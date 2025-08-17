package br.com.dillmann.fireflycompanion.business.assistant

interface AssistantRepositoryProvider {
    suspend fun provide(baseUrl: String, accessToken: String?) : AssistantRepository
}
