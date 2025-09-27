package br.com.dillmann.fireflycompanion.thirdparty.subscription

import br.com.dillmann.fireflycompanion.business.subscription.SubscriptionRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.core.converter.getConverter
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.BillsApi
import org.koin.dsl.module

internal val SubscriptionModule =
    module {
        single { BillsApi(get(Qualifiers.API_BASE_URL), get()) }
        single<SubscriptionConverter> { getConverter() }
        single<SubscriptionRepository> { SubscriptionHttpRepository(get(), get(),) }
    }
