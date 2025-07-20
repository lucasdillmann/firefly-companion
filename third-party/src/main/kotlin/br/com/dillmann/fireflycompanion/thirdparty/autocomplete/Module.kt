package br.com.dillmann.fireflycompanion.thirdparty.autocomplete

import br.com.dillmann.fireflycompanion.business.autocomplete.AutoCompleteRepository
import br.com.dillmann.fireflycompanion.thirdparty.core.Qualifiers
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AutocompleteApi
import org.koin.dsl.module

internal val AutoCompleteModule =
    module {
        single { AutocompleteApi(get(Qualifiers.API_BASE_URL), get()) }
        single<AutoCompleteRepository> { AutoCompleteHttpRepository(get()) }
    }
