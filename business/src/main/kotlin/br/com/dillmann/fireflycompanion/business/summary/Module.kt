package br.com.dillmann.fireflycompanion.business.summary

import br.com.dillmann.fireflycompanion.business.summary.usecase.GetSummaryUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val SummaryModule =
    module {
        single { SummaryService(get(), get()) } binds arrayOf(
            GetSummaryUseCase::class,
        )
    }
