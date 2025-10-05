package br.com.dillmann.fireflycompanion.thirdparty.summary

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.overview.model.ExpensesByCategoryOverview
import br.com.dillmann.fireflycompanion.business.overview.model.SummaryOverview
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.BasicSummaryEntry
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.InsightGroupEntry
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import java.math.BigDecimal

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface SummaryConverter {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "amount", source = "differenceFloat")
    fun toDomain(input: InsightGroupEntry): ExpensesByCategoryOverview

    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "balance", expression = "java(getValue(\"balance\", currency, summary))")
    @Mapping(target = "spent", expression = "java(getValue(\"spent\", currency, summary))")
    @Mapping(target = "earned", expression = "java(getValue(\"earned\", currency, summary))")
    @Mapping(target = "billsPaid", expression = "java(getValue(\"bills-paid\", currency, summary))")
    @Mapping(target = "unpaidBills", expression = "java(getValue(\"bills-unpaid\", currency, summary))")
    @Mapping(target = "leftToSpend", expression = "java(getValue(\"left-to-spend\", currency, summary))")
    @Mapping(target = "netWorth", expression = "java(getValue(\"net-worth\", currency, summary))")
    @Mapping(target = "reconciliations", source = "reconciliationAmount")
    fun toDomain(
        currency: Currency,
        summary: Map<String, BasicSummaryEntry>,
        reconciliationAmount: BigDecimal,
    ): SummaryOverview

    fun getValue(type: String, currency: Currency, data: Map<String, BasicSummaryEntry>): BigDecimal? =
        data["$type-in-${currency.code}"]?.monetaryValue
}
