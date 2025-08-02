package br.com.dillmann.fireflycompanion.business.account

import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountOverviewUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.GetAccountUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.ListAccountsUseCase
import br.com.dillmann.fireflycompanion.business.account.usecase.UpdateAccountBalanceUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val AccountModule =
    module {
        single { AccountService(get(), get(), get()) } binds arrayOf(
            ListAccountsUseCase::class,
            UpdateAccountBalanceUseCase::class,
            GetAccountUseCase::class,
            GetAccountOverviewUseCase::class,
        )
    }
