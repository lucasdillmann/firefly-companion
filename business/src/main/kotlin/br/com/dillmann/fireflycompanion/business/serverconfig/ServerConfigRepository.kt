package br.com.dillmann.fireflycompanion.business.serverconfig

interface ServerConfigRepository {
    suspend fun getConfig(): ServerConfig?
    suspend fun saveConfig(serverConfig: ServerConfig)
}
