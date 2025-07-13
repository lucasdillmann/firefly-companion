package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val TransactionModule =
    module {
        single { TransactionService(get()) } binds arrayOf(
            ListTransactionsUseCase::class,
            SearchTransactionsUseCase::class,
        )
    }
