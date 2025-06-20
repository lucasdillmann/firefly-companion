package br.com.dillmann.fireflymobile.business.serverconfig

interface ServerConfigCommands {
    fun getConfig(): ServerConfig?
    fun saveConfig(serverConfig: ServerConfig)
}
