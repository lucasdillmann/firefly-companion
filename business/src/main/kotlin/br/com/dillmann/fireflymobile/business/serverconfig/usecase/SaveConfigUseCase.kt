package br.com.dillmann.fireflymobile.business.serverconfig.usecase

import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfig

fun interface SaveConfigUseCase {
    fun saveConfig(serverConfig: ServerConfig)
}
