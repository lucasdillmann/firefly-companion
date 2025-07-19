package br.com.dillmann.fireflycompanion.business.transaction

import br.com.dillmann.fireflycompanion.business.transaction.usecase.DeleteTransactionUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.ListTransactionsUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SaveTransactionUseCase
import br.com.dillmann.fireflycompanion.business.transaction.usecase.SearchTransactionsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val TransactionModule =
    module {
        single { TransactionValidator() }
        single { TransactionService(get(), get()) } binds arrayOf(
            ListTransactionsUseCase::class,
            SearchTransactionsUseCase::class,
            SaveTransactionUseCase::class,
            DeleteTransactionUseCase::class,
        )
    }
