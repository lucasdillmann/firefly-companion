package br.com.dillmann.fireflycompanion.business

import br.com.dillmann.fireflycompanion.business.account.AccountModule
import br.com.dillmann.fireflycompanion.business.autocomplete.AutoCompleteModule
import br.com.dillmann.fireflycompanion.business.connectiontest.ConnectionTestModule
import br.com.dillmann.fireflycompanion.business.currency.CurrencyModule
import br.com.dillmann.fireflycompanion.business.overview.OverviewModule
import br.com.dillmann.fireflycompanion.business.preferences.PreferencesModule
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfigModule
import br.com.dillmann.fireflycompanion.business.transaction.TransactionModule
import br.com.dillmann.fireflycompanion.business.user.UserModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val BusinessModule =
    module {
        loadKoinModules(AccountModule)
        loadKoinModules(AutoCompleteModule)
        loadKoinModules(ConnectionTestModule)
        loadKoinModules(CurrencyModule)
        loadKoinModules(PreferencesModule)
        loadKoinModules(ServerConfigModule)
        loadKoinModules(OverviewModule)
        loadKoinModules(TransactionModule)
        loadKoinModules(UserModule)
    }
