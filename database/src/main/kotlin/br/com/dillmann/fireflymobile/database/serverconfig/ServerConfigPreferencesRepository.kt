package br.com.dillmann.fireflymobile.database.serverconfig

import android.content.Context
import androidx.core.content.edit
import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfig
import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfigRepository

internal class ServerConfigPreferencesRepository(private val context: Context) : ServerConfigRepository {
    companion object {
        private const val PREFERENCES_NAME = "server_config_preferences"
        private const val KEY_URL = "server_url"
        private const val KEY_TOKEN = "server_token"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override fun getConfig(): ServerConfig? {
        val url = sharedPreferences.getString(KEY_URL, null)
        val token = sharedPreferences.getString(KEY_TOKEN, null)

        return if (url == null || token == null) null
        else ServerConfig(url, token)
    }

    override fun saveConfig(serverConfig: ServerConfig) {
        sharedPreferences.edit {
            putString(KEY_URL, serverConfig.url)
            putString(KEY_TOKEN, serverConfig.token)
        }
    }
}
