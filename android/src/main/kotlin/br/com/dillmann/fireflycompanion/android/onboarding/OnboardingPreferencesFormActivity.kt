package br.com.dillmann.fireflycompanion.android.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.home.HomeActivity
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingPreferencesFormButtons
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingPreferencesFormFields
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingPreferencesHeader
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.ui.activity.async
import br.com.dillmann.fireflycompanion.android.ui.activity.start
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin

class OnboardingPreferencesFormActivity : PreconfiguredActivity() {
    private val getPreferences = getKoin().get<GetPreferencesUseCase>()

    @Composable
    override fun Content(padding: PaddingValues) {
        val currentPreferences = runBlocking { getPreferences.getPreferences() }
        val requireBiometricLogin = remember { mutableStateOf(currentPreferences.requireBiometricLogin) }
        val theme = remember { mutableStateOf(currentPreferences.theme) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            OnboardingPreferencesHeader()
            OnboardingPreferencesFormFields(
                requireBiometricLogin = requireBiometricLogin,
                theme = theme,
                onThemeChanged = {
                    submitPreferences(requireBiometricLogin.value, theme.value)
                    recreate()
                }
            )
            OnboardingPreferencesFormButtons {
                submitPreferences(requireBiometricLogin.value, theme.value)
                start<HomeActivity>()
            }
        }
    }

    private fun submitPreferences(
        requireBiometricLogin: Boolean,
        theme: Preferences.Theme,
    ) {
        val savePreferences = getKoin().get<SavePreferencesUseCase>()
        val preferences = Preferences(requireBiometricLogin, theme)

        async(wait = true) { savePreferences.savePreferences(preferences) }
    }
}
