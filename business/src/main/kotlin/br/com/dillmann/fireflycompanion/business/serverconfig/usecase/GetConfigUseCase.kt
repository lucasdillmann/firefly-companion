package br.com.dillmann.fireflycompanion.business.serverconfig.usecase

import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfig

fun interface GetConfigUseCase {
    suspend fun getConfig(): ServerConfig?
}
