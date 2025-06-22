package br.com.dillmann.fireflycompanion.business.user

import org.koin.dsl.module

internal val UserModule =
    module {
        single { UserService(get()) }
    }
