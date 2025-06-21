package br.com.dillmann.fireflymobile.business.serverconfig.usecase

import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfig

fun interface GetConfigUseCase {
    fun getConfig(): ServerConfig?
}
