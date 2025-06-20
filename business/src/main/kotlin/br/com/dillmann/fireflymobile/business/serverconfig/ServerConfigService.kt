package br.com.dillmann.fireflymobile.business.serverconfig

internal class ServerConfigService(
    private val repository: ServerConfigRepository,
    private val validator: ServerConfigValidator,
) : ServerConfigCommands {
    override fun getConfig(): ServerConfig? {
        return repository.getConfig()
    }

    override fun saveConfig(serverConfig: ServerConfig) {
        repository.saveConfig(serverConfig)
    }
}
