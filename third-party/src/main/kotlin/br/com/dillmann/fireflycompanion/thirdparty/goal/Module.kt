package br.com.dillmann.fireflycompanion.thirdparty.goal

import br.com.dillmann.fireflycompanion.business.goal.GoalRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.converter.getConverter
import org.koin.dsl.module

internal val GoalModule =
    module {
        single<GoalConverter> { getConverter() }
        single<GoalRepository> { GoalHttpRepository(get(), get()) }
    }
