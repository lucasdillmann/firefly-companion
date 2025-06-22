package br.com.dillmann.fireflycompanion.business.serverconfig

interface ServerConfigRepository {
    fun getConfig(): ServerConfig?
    fun saveConfig(serverConfig: ServerConfig)
}
