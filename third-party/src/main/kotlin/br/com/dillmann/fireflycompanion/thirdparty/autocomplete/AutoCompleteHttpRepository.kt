package br.com.dillmann.fireflycompanion.thirdparty.autocomplete

import br.com.dillmann.fireflycompanion.business.autocomplete.AutoCompleteRepository
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AutocompleteApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.models.AccountTypeFilter

internal class AutoCompleteHttpRepository(private val api: AutocompleteApi) : AutoCompleteRepository {
    override suspend fun findAccountSuggestions(searchTerms: String, limit: Int, types: List<String>): List<String> =
        api.getAccountsAC(
            query = searchTerms,
            limit = limit,
            types = types.map { AccountTypeFilter.decode(it)!! }.toMutableList(),
        ).map {
            it.name
        }.sorted()

    override suspend fun findCategorySuggestions(searchTerms: String, limit: Int): List<String> =
        api.getCategoriesAC(query = searchTerms, limit = limit).map { it.name }.sorted()

    override suspend fun findDescriptionSuggestions(searchTerms: String, limit: Int): List<String> =
        api.getTransactionsAC(query = searchTerms, limit = limit).map { it.description }.sorted()

    override suspend fun findTagSuggestions(searchTerms: String, limit: Int): List<String> =
        api.getTagAC(query = searchTerms, limit = limit).map { it.name }.sorted()
}
