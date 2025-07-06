package br.com.dillmann.fireflycompanion.database.serverconfig

import android.content.Context
import androidx.core.content.edit
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfig
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfigRepository
import br.com.dillmann.fireflycompanion.database.context.ContextProvider

internal class ServerConfigPreferencesRepository(private val contextProvider: ContextProvider) : ServerConfigRepository {
    companion object {
        private const val PREFERENCES_NAME = "server_config_preferences"
        private const val KEY_URL = "server_url"
        private const val KEY_TOKEN = "server_token"
    }

    override suspend fun getConfig(): ServerConfig? {
        val sharedPreferences = resolvePreferences()
        val url = sharedPreferences.getString(KEY_URL, null)
        val token = sharedPreferences.getString(KEY_TOKEN, null)

        return if (url == null || token == null) null
        else ServerConfig(url, token)
    }

    override suspend fun saveConfig(serverConfig: ServerConfig) {
        resolvePreferences().edit {
            putString(KEY_URL, serverConfig.url)
            putString(KEY_TOKEN, serverConfig.token)
        }
    }

    private suspend fun resolvePreferences() =
        contextProvider.resolve().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}
