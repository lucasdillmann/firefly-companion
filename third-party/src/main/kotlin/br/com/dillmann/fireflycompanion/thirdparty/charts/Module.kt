package br.com.dillmann.fireflycompanion.thirdparty.charts

import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.ChartsApi
import org.koin.dsl.module

internal val ChartsModule =
    module {
        single { ChartsApi(get(Qualifiers.API_BASE_URL), get()) }
    }
