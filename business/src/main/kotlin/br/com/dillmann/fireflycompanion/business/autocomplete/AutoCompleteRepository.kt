package br.com.dillmann.fireflycompanion.business.autocomplete

interface AutoCompleteRepository {
    suspend fun findAccountSuggestions(searchTerms: String, limit: Int, types: List<String>): List<String>
    suspend fun findCategorySuggestions(searchTerms: String, limit: Int): List<String>
    suspend fun findDescriptionSuggestions(searchTerms: String, limit: Int): List<String>
    suspend fun findTagSuggestions(searchTerms: String, limit: Int): List<String>
}
