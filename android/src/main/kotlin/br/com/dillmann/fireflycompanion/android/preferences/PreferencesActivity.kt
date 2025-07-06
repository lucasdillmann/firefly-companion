package br.com.dillmann.fireflycompanion.android.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.home.HomeActivity
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormButtons
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormFields
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesHeader
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin

class PreferencesActivity : PreconfiguredActivity() {
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
            PreferencesHeader()
            PreferencesFormFields(
                requireBiometricLogin = requireBiometricLogin,
                theme = theme,
                onThemeChanged = {
                    submitPreferences(requireBiometricLogin.value, theme.value)
                    recreate()
                }
            )
            PreferencesFormButtons {
                submitPreferences(requireBiometricLogin.value, theme.value)
                finish()
            }
        }
    }

    private fun submitPreferences(
        requireBiometricLogin: Boolean,
        theme: Preferences.Theme,
    ) {
        val savePreferences = getKoin().get<SavePreferencesUseCase>()
        val preferences = Preferences(requireBiometricLogin, theme)

        runBlocking { savePreferences.savePreferences(preferences) }
    }
}
