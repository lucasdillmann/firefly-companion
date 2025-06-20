package br.com.dillmann.fireflymobile.firefly

import br.com.dillmann.fireflymobile.business.serverconfig.ServerConfigCommands
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

internal class FireflyAuthenticator(private val commands: ServerConfigCommands): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val authToken = commands.getConfig()?.token ?: response.request

        return response
            .request
            .newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()
    }
}
