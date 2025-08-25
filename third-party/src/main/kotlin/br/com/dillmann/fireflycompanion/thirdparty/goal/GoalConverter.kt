package br.com.dillmann.fireflycompanion.thirdparty.goal

import br.com.dillmann.fireflycompanion.business.goal.Goal
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.PiggyBankRead
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface GoalConverter {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "attributes.name")
    @Mapping(target = "startDate", source = "attributes.startDate", defaultExpression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "targetDate", source = "attributes.targetDate", defaultExpression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "currentAmount", source = "attributes.currentAmount", defaultExpression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "targetAmount", source = "attributes.targetAmount", defaultExpression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "currency.id", source = "attributes.currencyId", defaultValue = "")
    @Mapping(target = "currency.code", source = "attributes.currencyCode", defaultValue = "USD")
    @Mapping(target = "currency.symbol", source = "attributes.currencySymbol", defaultValue = "$")
    @Mapping(target = "currency.decimalPlaces", source = "attributes.currencyDecimalPlaces", defaultValue = "2")
    @Mapping(target = "active", source = "attributes.active", defaultValue = "false")
    fun toDomain(account: PiggyBankRead): Goal
}
