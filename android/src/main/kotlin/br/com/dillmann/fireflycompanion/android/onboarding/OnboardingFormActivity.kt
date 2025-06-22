package br.com.dillmann.fireflycompanion.android.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingFormFields
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingFormHeader
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingFormButtons
import br.com.dillmann.fireflycompanion.android.ui.activity.PreconfiguredActivity

class OnboardingFormActivity : PreconfiguredActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content(padding: PaddingValues) {
        val scrollState = rememberScrollState()
        val serverUrl = remember { mutableStateOf(TextFieldValue()) }
        val accessToken = remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            OnboardingFormHeader()
            OnboardingFormFields(scrollState, serverUrl, accessToken)
            OnboardingFormButtons {

            }
        }
    }
}




