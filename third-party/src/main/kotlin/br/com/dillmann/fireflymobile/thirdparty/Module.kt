package br.com.dillmann.fireflymobile.thirdparty

import br.com.dillmann.fireflymobile.business.account.AccountRepository
import br.com.dillmann.fireflymobile.thirdparty.account.AccountHttpRepository
import org.koin.dsl.module

val ThirdPartyModule =
    module {
        single<AccountRepository> { AccountHttpRepository(get()) }
    }
