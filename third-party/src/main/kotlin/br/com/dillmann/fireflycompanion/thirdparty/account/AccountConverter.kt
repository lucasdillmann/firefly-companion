package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountRead
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface AccountConverter {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "attributes.type")
    @Mapping(target = "name", source = "attributes.name")
    @Mapping(target = "active", source = "attributes.active", defaultValue = "false")
    @Mapping(
        target = "currentBalance",
        source = "attributes.currentBalance",
        defaultExpression = "java(java.math.BigDecimal.ZERO)"
    )
    @Mapping(
        target = "openingBalance",
        source = "attributes.openingBalance",
        defaultExpression = "java(java.math.BigDecimal.ZERO)"
    )
    @Mapping(target = "includeInNetWorth", source = "attributes.includeNetWorth", defaultValue = "false")
    @Mapping(target = "currency.id", source = "attributes.currencyId", defaultValue = "")
    @Mapping(target = "currency.code", source = "attributes.currencyCode", defaultValue = "USD")
    @Mapping(target = "currency.symbol", source = "attributes.currencySymbol", defaultValue = "$")
    @Mapping(target = "currency.decimalPlaces", source = "attributes.currencyDecimalPlaces", defaultValue = "2")
    fun toDomain(account: AccountRead): Account
}
