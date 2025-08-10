package br.com.dillmann.fireflycompanion.core

import br.com.dillmann.fireflycompanion.core.json.JsonModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val CoreModule =
    module {
        loadKoinModules(JsonModule)
    }
