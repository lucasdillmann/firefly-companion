package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.AccountRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AccountsApi
import org.koin.dsl.module

internal val AccountModule =
    module {
        single { AccountsApi(get(Qualifiers.API_BASE_URL), get()) }
        single<AccountRepository> { AccountHttpRepository(get()) }
    }
