package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.summary.Summary
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.BasicSummaryEntry
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import java.math.BigDecimal

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface SummaryConverter {

    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "balance", expression = "java(getValue(\"balance\", currency, data))")
    @Mapping(target = "spent", expression = "java(getValue(\"spent\", currency, data))")
    @Mapping(target = "earned", expression = "java(getValue(\"earned\", currency, data))")
    @Mapping(target = "billsPaid", expression = "java(getValue(\"bills-paid\", currency, data))")
    @Mapping(target = "unpaidBills", expression = "java(getValue(\"bills-unpaid\", currency, data))")
    @Mapping(target = "leftToSpend", expression = "java(getValue(\"left-to-spend\", currency, data))")
    @Mapping(target = "netWorth", expression = "java(getValue(\"net-worth\", currency, data))")
    fun toDomain(currency: Currency, data: Map<String, BasicSummaryEntry>): Summary

    fun getValue(type: String, currency: Currency, data: Map<String, BasicSummaryEntry>): BigDecimal? =
        data["$type-in-${currency.code}"]?.monetaryValue
}
