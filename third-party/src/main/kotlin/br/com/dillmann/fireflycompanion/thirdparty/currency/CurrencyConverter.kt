package br.com.dillmann.fireflycompanion.thirdparty.currency

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.CurrencySingle
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface CurrencyConverter {

    @Mapping(source = "data.id", target = "id")
    @Mapping(source = "data.attributes.code", target = "code")
    @Mapping(source = "data.attributes.symbol", target = "symbol")
    @Mapping(source = "data.attributes.decimalPlaces", target = "decimalPlaces", defaultValue = "2")
    fun toDomain(input: CurrencySingle): Currency
}
