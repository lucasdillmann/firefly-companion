package br.com.dillmann.fireflymobile.business.serverconfig

interface ServerConfigRepository {
    fun getConfig(): ServerConfig?
    fun saveConfig(serverConfig: ServerConfig)
}
