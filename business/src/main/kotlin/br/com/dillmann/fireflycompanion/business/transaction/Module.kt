package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.GetTransactionsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val TransactionModule =
    module {
        single { TransactionService(get()) } binds arrayOf(
            GetTransactionsUseCase::class,
        )
    }
