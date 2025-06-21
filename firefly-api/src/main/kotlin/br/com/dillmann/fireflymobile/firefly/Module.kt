package br.com.dillmann.fireflymobile.firefly

import br.com.dillmann.fireflymobile.business.serverconfig.usecase.GetConfigUseCase
import br.com.dillmann.fireflymobile.firefly.apis.AccountsApi
import okhttp3.Call
import okhttp3.OkHttpClient
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

private const val URL = "firefly.serverUrl"
private const val TOKEN = "firefly.accessToken"

val FireflyModule =
    module {
        single(qualifier(URL)) {
            val host = get<GetConfigUseCase>().getConfig()?.url ?: ""
            "$host/api"
        }
        single { FireflyAuthenticator(get()) }
        single<Call.Factory> { OkHttpClient.Builder().authenticator(get<FireflyAuthenticator>()).build() }

        single { AccountsApi(get(qualifier(URL)), get()) }
    }
