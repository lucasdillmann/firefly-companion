package br.com.dillmann.fireflycompanion.thirdparty.insight

import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.InsightApi
import org.koin.dsl.module

internal val InsightModule =
    module {
        single { InsightApi(get(Qualifiers.API_BASE_URL), get()) }
    }
