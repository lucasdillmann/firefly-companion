package br.com.dillmann.fireflycompanion.core.json

import org.koin.dsl.module

internal val JsonModule =
    module {
        single<JsonConverter> { MoshiJsonConverter() }
    }
