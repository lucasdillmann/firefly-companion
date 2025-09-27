package br.com.dillmann.fireflycompanion.business

import br.com.dillmann.fireflycompanion.business.account.AccountModule
import br.com.dillmann.fireflycompanion.business.assistant.AssistantModule
import br.com.dillmann.fireflycompanion.business.autocomplete.AutoCompleteModule
import br.com.dillmann.fireflycompanion.business.connectiontest.ConnectionTestModule
import br.com.dillmann.fireflycompanion.business.currency.CurrencyModule
import br.com.dillmann.fireflycompanion.business.goal.GoalModule
import br.com.dillmann.fireflycompanion.business.overview.OverviewModule
import br.com.dillmann.fireflycompanion.business.preferences.PreferencesModule
import br.com.dillmann.fireflycompanion.business.subscription.SubscriptionModule
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfigModule
import br.com.dillmann.fireflycompanion.business.transaction.TransactionModule
import br.com.dillmann.fireflycompanion.business.user.UserModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val BusinessModule =
    module {
        loadKoinModules(AccountModule)
        loadKoinModules(AssistantModule)
        loadKoinModules(AutoCompleteModule)
        loadKoinModules(ConnectionTestModule)
        loadKoinModules(CurrencyModule)
        loadKoinModules(GoalModule)
        loadKoinModules(PreferencesModule)
        loadKoinModules(ServerConfigModule)
        loadKoinModules(SubscriptionModule)
        loadKoinModules(OverviewModule)
        loadKoinModules(TransactionModule)
        loadKoinModules(UserModule)
    }
