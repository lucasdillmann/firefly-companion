package br.com.dillmann.fireflymobile.business.serverconfig.command

import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfig

fun interface GetServerConfigCommand {
    fun invoke(): ServerConfig?
}
