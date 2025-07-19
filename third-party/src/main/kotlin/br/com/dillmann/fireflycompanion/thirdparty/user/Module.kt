package br.com.dillmann.fireflycompanion.thirdparty.user

import br.com.dillmann.fireflycompanion.business.user.UserRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.converter.getConverter
import org.koin.dsl.module

internal val UserModule =
    module {
        single<UserConverter> { getConverter() }
        single<UserRepository> { UserHttpRepository(get(), get()) }
    }
