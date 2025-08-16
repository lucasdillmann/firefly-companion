package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.overview.OverviewRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.core.converter.getConverter
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.SummaryApi
import org.koin.dsl.module

internal val SummaryModule =
    module {
        single { SummaryApi(get(Qualifiers.API_BASE_URL), get()) }
        single<SummaryConverter> { getConverter() }
        single<OverviewRepository> { OverviewHttpRepository(get(), get(),get()) }
    }
