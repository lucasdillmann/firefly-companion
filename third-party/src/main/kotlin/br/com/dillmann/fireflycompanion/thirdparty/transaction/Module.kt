package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.transaction.TransactionRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.TransactionsApi
import org.koin.dsl.module

internal val TransactionModule =
    module {
        single { TransactionsApi(get(Qualifiers.API_BASE_URL), get()) }
        single<TransactionRepository> { TransactionHttpRepository(get(), get()) }
    }
