package br.com.dillmann.fireflycompanion.thirdparty.subscription

import br.com.dillmann.fireflycompanion.business.subscription.Subscription
import br.com.dillmann.fireflycompanion.business.subscription.Subscription.Payment
import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.BillProperties
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.BillRead
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy
import java.math.BigDecimal

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface SubscriptionConverter {
    fun toDomain(input: BillRead): Subscription {
        val attributes = input.attributes
        val lastPayment = attributes
            .paidDates
            ?.filter { it.date != null && it.amount != null }
            ?.maxByOrNull { it.date!! }
            ?.let { Payment(it.date!!.toLocalDate(), it.amount!!) }
        val expectedPayment = attributes
            .nextExpectedMatch
            ?.let { Payment(it.toLocalDate(), attributes.amountAvg ?: BigDecimal.ZERO) }

        return Subscription(
            name = attributes.name ?: "",
            active = attributes.active ?: false,
            currency = toDomainCurrency(attributes),
            lastPayment = lastPayment,
            expectedPayment = expectedPayment,
        )
    }

    @Mapping(target = "id", source = "currencyId", defaultValue = "")
    @Mapping(target = "code", source = "currencyCode", defaultValue = "USD")
    @Mapping(target = "symbol", source = "currencySymbol", defaultValue = "$")
    @Mapping(target = "decimalPlaces", source = "currencyDecimalPlaces", defaultValue = "2")
    fun toDomainCurrency(attributes: BillProperties): Currency
}
