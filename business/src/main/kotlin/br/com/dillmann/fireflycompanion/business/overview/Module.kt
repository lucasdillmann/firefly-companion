package br.com.dillmann.fireflycompanion.business.overview

import br.com.dillmann.fireflycompanion.business.overview.usecase.ExpensesByCategoryOverviewUseCase
import br.com.dillmann.fireflycompanion.business.overview.usecase.SummaryOverviewUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val OverviewModule =
    module {
        single { OverviewService(get(), get()) } binds arrayOf(
            SummaryOverviewUseCase::class,
            ExpensesByCategoryOverviewUseCase::class,
        )
    }
