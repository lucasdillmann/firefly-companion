package br.com.dillmann.fireflycompanion.business.assistant.usecase

interface GetAvailableModelsUseCase {
    suspend fun getAvailableModels(): List<String>
}
