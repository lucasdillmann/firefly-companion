package br.com.dillmann.fireflycompanion.business.autocomplete.usecase

import br.com.dillmann.fireflycompanion.business.autocomplete.AutoCompleteType

interface AutoCompleteUseCase {
    suspend fun getSuggestions(
        type: AutoCompleteType,
        searchTerms: String,
        limit: Int = 25,
    ): List<String>
}
