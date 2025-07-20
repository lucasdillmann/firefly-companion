package br.com.dillmann.fireflycompanion.thirdparty

import br.com.dillmann.fireflycompanion.thirdparty.about.AboutModule
import br.com.dillmann.fireflycompanion.thirdparty.account.AccountModule
import br.com.dillmann.fireflycompanion.thirdparty.autocomplete.AutoCompleteModule
import br.com.dillmann.fireflycompanion.thirdparty.connectiontest.ConnectionTestModule
import br.com.dillmann.fireflycompanion.thirdparty.core.CoreModule
import br.com.dillmann.fireflycompanion.thirdparty.currency.CurrencyModule
import br.com.dillmann.fireflycompanion.thirdparty.search.SearchModule
import br.com.dillmann.fireflycompanion.thirdparty.summary.SummaryModule
import br.com.dillmann.fireflycompanion.thirdparty.transaction.TransactionModule
import br.com.dillmann.fireflycompanion.thirdparty.user.UserModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val ThirdPartyModule =
    module {
        loadKoinModules(CoreModule)
        loadKoinModules(AboutModule)
        loadKoinModules(AccountModule)
        loadKoinModules(AutoCompleteModule)
        loadKoinModules(ConnectionTestModule)
        loadKoinModules(CurrencyModule)
        loadKoinModules(SummaryModule)
        loadKoinModules(SearchModule)
        loadKoinModules(TransactionModule)
        loadKoinModules(UserModule)
    }
