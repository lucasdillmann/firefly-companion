package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AccountModule =
    module {
        single { AccountService(get()) } binds arrayOf(ListAccountsUseCase::class)
    }
