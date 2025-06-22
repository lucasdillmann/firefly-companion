package br.com.dillmann.fireflycompanion.thirdparty.about

import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AboutApi
import org.koin.dsl.module

internal val AboutModule =
    module {
        single { AboutApi(get(Qualifiers.API_BASE_URL), get()) }
    }
