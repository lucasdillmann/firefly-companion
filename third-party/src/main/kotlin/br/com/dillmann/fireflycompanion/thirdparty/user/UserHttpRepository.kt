package br.com.dillmann.fireflycompanion.thirdparty.user

import br.com.dillmann.fireflycompanion.business.user.User
import br.com.dillmann.fireflycompanion.business.user.UserRepository
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AboutApi

internal class UserHttpRepository(
    private val aboutApi: AboutApi,
    private val converter: UserConverter,
) : UserRepository {
    override suspend fun getCurrent(): User {
        val user = aboutApi.getCurrentUser()
        return converter.toDomain(user)
    }
}
