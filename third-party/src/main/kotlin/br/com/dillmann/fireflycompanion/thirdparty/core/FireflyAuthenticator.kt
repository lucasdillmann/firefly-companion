package br.com.dillmann.fireflycompanion.thirdparty.core

import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

internal class FireflyAuthenticator(private val commands: GetConfigUseCase): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val authToken = commands.getConfig()?.token ?: return response.request

        return response
            .request
            .newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()
    }
}
