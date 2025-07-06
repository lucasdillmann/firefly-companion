package br.com.dillmann.fireflycompanion.business.serverconfig.usecase

import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfig

interface SaveConfigUseCase {
    suspend fun saveConfig(serverConfig: ServerConfig)
}
