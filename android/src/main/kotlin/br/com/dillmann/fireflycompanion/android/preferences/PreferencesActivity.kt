package br.com.dillmann.fireflycompanion.android.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.home.HomeActivity
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormButtons
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesFormFields
import br.com.dillmann.fireflycompanion.android.preferences.components.PreferencesHeader
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import br.com.dillmann.fireflycompanion.business.preferences.usecase.SavePreferencesUseCase
import kotlinx.coroutines.runBlocking

class PreferencesActivity : PreconfiguredActivity() {
    private val getPreferences = koin().get<GetPreferencesUseCase>()

    @Composable
    override fun Content(padding: PaddingValues) {
        val context = LocalContext.current
        val currentPreferences = runBlocking { getPreferences.getPreferences() }
        val state = remember { mutableStateOf(currentPreferences) }

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
                state = state,
                onChange = {
                    submitPreferences(state.value)
                    recreate()
                }
            )
            PreferencesFormButtons {
                submitPreferences(state.value)
                context.start<HomeActivity>(
                    finish = true,
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
        }
    }
}
