package br.com.dillmann.fireflycompanion.thirdparty.account

import br.com.dillmann.fireflycompanion.business.account.Account
import br.com.dillmann.fireflycompanion.business.account.AccountOverview
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountRead
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.ChartDataSet
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy
import java.math.BigDecimal
import java.time.OffsetDateTime

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

    @Mapping(target = "name", source = "label")
    @Mapping(target = "currency.id", source = "currencyId", defaultValue = "")
    @Mapping(target = "currency.code", source = "currencyCode", defaultValue = "USD")
    @Mapping(target = "currency.symbol", source = "currencySymbol", defaultValue = "$")
    @Mapping(target = "currency.decimalPlaces", source = "currencyDecimalPlaces", defaultValue = "2")
    @Mapping(target = "balanceHistory", source = "input", qualifiedByName = ["toDomainDateValueMap"])
    fun toDomain(input: ChartDataSet): AccountOverview

    @Named("toDomainDateValueMap")
    fun toDomainDateValueMap(input: ChartDataSet): Map<OffsetDateTime, BigDecimal> =
        input
            .propertyEntries
            ?.mapKeys { OffsetDateTime.parse(it.key) }
            ?: emptyMap()
}
