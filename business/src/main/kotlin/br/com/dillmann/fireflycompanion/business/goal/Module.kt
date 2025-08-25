package br.com.dillmann.fireflycompanion.business.goal

import br.com.dillmann.fireflycompanion.business.goal.usecase.ListGoalsUseCase
import org.koin.dsl.binds
import org.koin.dsl.module

internal val GoalModule =
    module {
        single { GoalService(get()) } binds arrayOf(
            ListGoalsUseCase::class,
        )
    }
