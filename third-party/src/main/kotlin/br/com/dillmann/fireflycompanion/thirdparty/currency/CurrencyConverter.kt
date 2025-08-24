package br.com.dillmann.fireflycompanion.thirdparty.currency

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.CurrencyRead
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface CurrencyConverter {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "attributes.code", target = "code")
    @Mapping(source = "attributes.symbol", target = "symbol")
    @Mapping(source = "attributes.decimalPlaces", target = "decimalPlaces", defaultValue = "2")
    fun toDomain(input: CurrencyRead): Currency
}
