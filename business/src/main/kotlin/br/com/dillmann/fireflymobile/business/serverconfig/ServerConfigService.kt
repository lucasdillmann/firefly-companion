package br.com.dillmann.fireflymobile.business.serverconfig

internal class ServerConfigService(
    private val repository: ServerConfigRepository,
    private val validator: ServerConfigValidator,
) {
    fun getConfig(): ServerConfig? {
        return repository.getConfig()
    }

    fun setConfig(serverConfig: ServerConfig) {
        repository.saveConfig(serverConfig)
    }
}
