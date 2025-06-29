package br.com.dillmann.fireflycompanion.business.serverconfig

import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.SaveConfigUseCase

internal class ServerConfigService(
    private val repository: ServerConfigRepository,
    private val validator: ServerConfigValidator,
) : GetConfigUseCase, SaveConfigUseCase {
    override suspend fun getConfig(): ServerConfig? {
        return repository.getConfig()
    }

    override suspend fun saveConfig(serverConfig: ServerConfig) {
        validator.validate(serverConfig)
        repository.saveConfig(serverConfig)
    }
}
