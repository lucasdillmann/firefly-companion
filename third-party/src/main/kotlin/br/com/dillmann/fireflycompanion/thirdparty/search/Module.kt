package br.com.dillmann.fireflycompanion.thirdparty.search

import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SearchApi
import org.koin.dsl.module

internal val SearchModule =
    module {
        single { SearchApi(get(Qualifiers.API_BASE_URL), get()) }
    }
