package br.com.dillmann.fireflymobile.business.account

import br.com.dillmann.fireflymobile.business.account.usecase.GetAccountsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AccountModule =
    module {
        single { AccountService(get()) } binds arrayOf(GetAccountsUseCase::class)
    }
