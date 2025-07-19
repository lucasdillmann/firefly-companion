package br.com.dillmann.fireflycompanion.thirdparty.user

import br.com.dillmann.fireflycompanion.business.user.User
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.UserSingle
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface UserConverter {

    @Mapping(source = "data.id", target = "id")
    @Mapping(source = "data.attributes.email", target = "name")
    fun toDomain(input: UserSingle): User
}
