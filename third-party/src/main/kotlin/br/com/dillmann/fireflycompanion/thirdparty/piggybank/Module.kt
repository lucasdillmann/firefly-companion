package br.com.dillmann.fireflycompanion.thirdparty.piggybank

import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.PiggyBanksApi
import org.koin.dsl.module

internal val PiggyBankModule =
    module {
        single { PiggyBanksApi(get(Qualifiers.API_BASE_URL), get()) }
    }
