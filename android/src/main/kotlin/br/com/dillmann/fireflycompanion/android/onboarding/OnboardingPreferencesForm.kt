package br.com.dillmann.fireflycompanion.android.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.core.theme.AppThemeContext
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingPreferencesHeader
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormButtons
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormFields
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import kotlinx.coroutines.runBlocking

@Composable
fun NavigationContext.OnboardingPreferencesForm() {
    val getPreferences = koin().get<GetPreferencesUseCase>()
    val currentPreferences = async { getPreferences.getPreferences() }.get()
    val preferences = volatile(currentPreferences)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        OnboardingPreferencesHeader()
        PreferencesFormFields(
            state = preferences,
            onChange = { submitPreferences(preferences.value) }
        )
        PreferencesFormButtons(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            saveText = i18n(R.string.finish),
        ) {
            submitPreferences(preferences.value)
            open(route = Route.HOME_SCREEN, finishCurrent = true)
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
