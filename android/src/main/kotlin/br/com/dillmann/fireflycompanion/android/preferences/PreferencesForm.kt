package br.com.dillmann.fireflycompanion.android.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.theme.AppThemeContext
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormButtons
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormFields
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesHeader
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import kotlinx.coroutines.runBlocking

@Composable
fun NavigationContext.PreferencesForm() {
    val getPreferences = koin().get<GetPreferencesUseCase>()
    val currentPreferences = async { getPreferences.getPreferences() }.join()
    val state = volatile(currentPreferences)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        PreferencesHeader()
        PreferencesFormFields(
            state = state,
            onChange = { submitPreferences(state.value) }
        )
        PreferencesFormButtons {
            submitPreferences(state.value)
            open(
                route = Route.HOME_SCREEN,
                finishCurrent = true,
                replacePrevious = true,
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
