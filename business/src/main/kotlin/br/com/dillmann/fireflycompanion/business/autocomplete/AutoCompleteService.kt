package br.com.dillmann.fireflycompanion.business.autocomplete

import br.com.dillmann.fireflycompanion.business.autocomplete.usecase.AutoCompleteUseCase

internal class AutoCompleteService(private val repository: AutoCompleteRepository) : AutoCompleteUseCase {
    override suspend fun getSuggestions(
        type: AutoCompleteType,
        searchTerms: String,
        limit: Int,
    ): List<String> =
        when (type) {
            AutoCompleteType.ACCOUNT -> repository.findAccountSuggestions(searchTerms, limit, listOf("Asset account"))
            AutoCompleteType.CATEGORY -> repository.findCategorySuggestions(searchTerms, limit)
            AutoCompleteType.DESCRIPTION -> repository.findDescriptionSuggestions(searchTerms, limit)
            AutoCompleteType.TAG -> repository.findTagSuggestions(searchTerms, limit)
        }
}
