package br.com.dillmann.fireflycompanion.thirdparty.user

import br.com.dillmann.fireflycompanion.business.user.User
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.UserSingle

internal fun UserSingle.toUser(): User =
    User(
        id = data.id,
        name = data.attributes.email,
    )
