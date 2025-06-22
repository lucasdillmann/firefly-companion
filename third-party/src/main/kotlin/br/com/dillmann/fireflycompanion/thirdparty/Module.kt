package br.com.dillmann.fireflycompanion.thirdparty

import br.com.dillmann.fireflycompanion.thirdparty.account.AccountModule
import br.com.dillmann.fireflycompanion.thirdparty.core.CoreModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val ThirdPartyModule =
    module {
        loadKoinModules(CoreModule)
        loadKoinModules(AccountModule)
    }
