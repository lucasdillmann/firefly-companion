package br.com.dillmann.fireflycompanion.business.subscription

import br.com.dillmann.fireflycompanion.business.subscription.usecase.SubscriptionOverviewUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val SubscriptionModule =
    module {
        single { SubscriptionService(get()) } binds arrayOf(
            SubscriptionOverviewUseCase::class,
        )
    }
