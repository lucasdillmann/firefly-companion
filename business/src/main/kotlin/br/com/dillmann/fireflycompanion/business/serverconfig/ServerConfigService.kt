package br.com.dillmann.fireflycompanion.business.serverconfig

import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.SaveConfigUseCase

internal class ServerConfigService(
    private val repository: ServerConfigRepository,
    private val validator: ServerConfigValidator,
) : GetConfigUseCase, SaveConfigUseCase {
    override fun getConfig(): ServerConfig? {
        return repository.getConfig()
    }

    override fun saveConfig(serverConfig: ServerConfig) {
        validator.validate(serverConfig)
        repository.saveConfig(serverConfig)
    }
}
