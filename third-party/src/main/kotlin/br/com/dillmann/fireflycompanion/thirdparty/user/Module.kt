package br.com.dillmann.fireflycompanion.thirdparty.user

import br.com.dillmann.fireflycompanion.business.user.UserRepository
import org.koin.dsl.module

internal val UserModule =
    module {
        single<UserRepository> { UserHttpRepository(get()) }
    }
