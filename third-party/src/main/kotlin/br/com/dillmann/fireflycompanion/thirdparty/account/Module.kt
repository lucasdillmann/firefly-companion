package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.AccountRepository
import org.koin.dsl.module

internal val AccountModule =
    module {
        single<AccountRepository> { AccountHttpRepository(get()) }
    }
