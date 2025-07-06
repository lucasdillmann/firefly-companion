package br.com.dillmann.fireflycompanion.android.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingServerFormButtons
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingServerFormFields
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingServerFormHeader
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfig
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.SaveConfigUseCase
import br.com.dillmann.fireflycompanion.core.validation.ConsistencyException
import br.com.dillmann.fireflycompanion.core.validation.MessageException
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome
import org.koin.android.ext.android.getKoin

class OnboardingServerFormActivity : PreconfiguredActivity() {
    @Composable
    override fun Content(padding: PaddingValues) {
        val scrollState = rememberScrollState()
        val serverUrl = remember { mutableStateOf(TextFieldValue()) }
        val accessToken = remember { mutableStateOf(TextFieldValue()) }
        val validationOutcome = remember { mutableStateOf<ValidationOutcome?>(null) }
        val showLoading = remember { mutableStateOf(false) }
        val errorDialog = remember { mutableStateOf<MessageException?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            OnboardingServerFormHeader()
            OnboardingServerFormFields(scrollState, serverUrl, accessToken, validationOutcome.value)
            OnboardingServerFormButtons {
                handleSubmit(
                    serverUrl.value.text,
                    accessToken.value.text,
                    validationOutcome,
                    showLoading,
                    errorDialog,
                )
            }
        }

        if (showLoading.value) {
            Dialog(onDismissRequest = {}) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Connecting to the Firefly III server...")
                }
            }
        }

        errorDialog.value?.let { exception ->
            AlertDialog(
                onDismissRequest = { errorDialog.value = null },
                title = { Text(text = exception.title) },
                text = { Text(text = exception.message ?: "") },
                confirmButton = {
                    TextButton(onClick = { errorDialog.value = null }) {
                        Text("Got it")
                    }
                }
            )
        }
    }

    private fun handleSubmit(
        serverUrl: String,
        accessToken: String,
        outcome: MutableState<ValidationOutcome?>,
        showLoading: MutableState<Boolean>,
        errorDialog: MutableState<MessageException?>,
    ) {
        val saveAction = getKoin().get<SaveConfigUseCase>()
        val config = ServerConfig(serverUrl, accessToken)

        outcome.value = null
        showLoading.value = true

        async {
            try {
                saveAction.saveConfig(config)
                start<OnboardingPreferencesFormActivity>()
                finish()
            } catch (ex: MessageException) {
                errorDialog.value = ex
            } catch (ex: ConsistencyException) {
                outcome.value = ex.outcome
            } finally {
                showLoading.value = false
            }
        }
    }
}
