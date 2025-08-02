package br.com.dillmann.fireflycompanion.thirdparty.transaction

import br.com.dillmann.fireflycompanion.business.currency.Currency
import br.com.dillmann.fireflycompanion.business.transaction.Transaction
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.*
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal interface TransactionConverter {

    @Mapping(target = "description", expression = "java(value(input, TransactionSplit::getDescription))")
    @Mapping(target = "date", expression = "java(value(input, TransactionSplit::getDate))")
    @Mapping(target = "amount", expression = "java(value(input, TransactionSplit::getAmount))")
    @Mapping(target = "type", expression = "java(value(input, TransactionSplit::getType, this::toDomain))")
    @Mapping(target = "category", expression = "java(value(input, TransactionSplit::getCategoryName))")
    @Mapping(target = "sourceAccountName", expression = "java(value(input, TransactionSplit::getSourceName))")
    @Mapping(target = "destinationAccountName", expression = "java(value(input, TransactionSplit::getDestinationName))")
    @Mapping(target = "currency", expression = "java(value(input, this::toDomain))")
    fun toDomain(input: TransactionRead): Transaction

    @Mapping(target = "id", source = "currencyId", defaultValue = "")
    @Mapping(target = "code", source = "currencyCode", defaultValue = "USD")
    @Mapping(target = "symbol", source = "currencySymbol", defaultValue = "$")
    @Mapping(target = "decimalPlaces", source = "currencyDecimalPlaces", defaultValue = "2")
    fun toDomain(split: TransactionSplit): Currency

    fun toDomain(input: TransactionTypeProperty): Transaction.Type

    @Mapping(target = "currencyCode", source = "currency.code")
    @Mapping(target = "sourceName", source = "sourceAccountName")
    @Mapping(target = "destinationName", source = "destinationAccountName")
    @Mapping(target = "categoryName", source = "category")
    fun toApiSplitStore(input: Transaction): TransactionSplitStore

    @Mapping(target = "currencyCode", source = "currency.code")
    @Mapping(target = "sourceName", source = "sourceAccountName")
    @Mapping(target = "destinationName", source = "destinationAccountName")
    @Mapping(target = "categoryName", source = "category")
    fun toApiSplitUpdate(input: Transaction): TransactionSplitUpdate

    fun toApiStore(input: Transaction): TransactionStore =
        TransactionStore(
            transactions = mutableListOf(toApiSplitStore(input)),
        )

    fun toApiUpdate(input: Transaction): TransactionUpdate =
        TransactionUpdate(
            transactions = mutableListOf(toApiSplitUpdate(input)),
        )

    fun <T> value(transaction: TransactionRead, selector: (TransactionSplit) -> T?): T? =
        transaction.attributes.transactions.first().let(selector)

    fun <I, O> value(transaction: TransactionRead, selector: (TransactionSplit) -> I?, transformer: (I) -> O): O? =
        value(transaction, selector)?.let(transformer)
}
