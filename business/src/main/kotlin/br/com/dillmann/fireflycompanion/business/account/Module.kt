package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AccountModule =
    module {
        single { AccountService(get()) } binds arrayOf(GetAccountsUseCase::class)
    }
