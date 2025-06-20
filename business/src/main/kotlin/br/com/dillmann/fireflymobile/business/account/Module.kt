package br.com.dillmann.fireflymobile.business.account

import org.koin.dsl.module

internal val AccountModule =
    module {
        single<AccountCommands> { AccountService(get()) }
    }
