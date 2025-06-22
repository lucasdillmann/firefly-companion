package br.com.dillmann.fireflycompanion.thirdparty.core

import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.GetConfigUseCase
import okhttp3.Call
import okhttp3.OkHttpClient
import org.koin.dsl.module

private const val URL = "firefly.serverUrl"

internal val CoreModule =
    module {
        single(Qualifiers.API_BASE_URL) {
            val host = get<GetConfigUseCase>().getConfig()?.url ?: ""
            "$host/api"
        }

        single { FireflyAuthenticator(get()) }
        single<Call.Factory> { OkHttpClient.Builder().authenticator(get<FireflyAuthenticator>()).build() }
    }
