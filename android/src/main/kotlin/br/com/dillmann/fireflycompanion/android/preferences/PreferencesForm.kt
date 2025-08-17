package br.com.dillmann.fireflycompanion.android.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.theme.AppThemeContext
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormButtons
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormFields
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import kotlinx.coroutines.runBlocking

@Composable
fun NavigationContext.PreferencesForm() {
    val getPreferences = koin().get<GetPreferencesUseCase>()
    val currentPreferences = async { getPreferences.getPreferences() }.join()
    val preferencesState = volatile(currentPreferences)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Section(
            modifier = Modifier.padding(top = 32.dp).fillMaxSize(),
            title = i18n(R.string.preferences),
            rightContent = {
                PreferencesFormButtons {
                    submitPreferences(preferencesState.value)
                    open(
                        route = Route.HOME_SCREEN,
                        finishCurrent = true,
                        replacePrevious = true,
                    )
                }
            }
        ) {
            PreferencesFormFields(
                state = preferencesState,
                onChange = { submitPreferences(preferencesState.value) }
            )
        }
    }
}

private fun submitPreferences(preferences: Preferences) {
    val savePreferences = koin().get<SavePreferencesUseCase>()

    runBlocking {
        savePreferences.savePreferences(preferences)
        AppContext.reconfigure(preferences)
        AppThemeContext.reconfigure(preferences)
    }
}
