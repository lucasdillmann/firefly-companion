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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.emptyVolatile
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.KoinManager.koin
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.android.core.router.Route
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingServerFormButtons
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingServerFormFields
import br.com.dillmann.fireflycompanion.android.onboarding.components.OnboardingServerFormHeader
import br.com.dillmann.fireflycompanion.business.serverconfig.ServerConfig
import br.com.dillmann.fireflycompanion.business.serverconfig.usecase.SaveConfigUseCase
import br.com.dillmann.fireflycompanion.core.validation.ConsistencyException
import br.com.dillmann.fireflycompanion.core.validation.MessageException
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome

@Composable
fun NavigationContext.OnboardingServerForm() {
    val scrollState = rememberScrollState()
    val serverUrl = volatile(TextFieldValue())
    val accessToken = volatile(TextFieldValue())
    val validationOutcome = emptyVolatile<ValidationOutcome?>()
    val showLoading = volatile(false)
    val errorDialog = emptyVolatile<MessageException>()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                Text(text = i18n(R.string.connecting_to_firefly_server))
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
                    Text(i18n(R.string.got_it))
                }
            }
        )
    }
}

private fun NavigationContext.handleSubmit(
    serverUrl: String,
    accessToken: String,
    outcome: MutableState<ValidationOutcome?>,
    showLoading: MutableState<Boolean>,
    errorDialog: MutableState<MessageException?>,
) {
    val saveAction = koin().get<SaveConfigUseCase>()
    val config = ServerConfig(serverUrl, accessToken)

    outcome.value = null
    showLoading.value = true

    async {
        try {
            saveAction.saveConfig(config)
            open(route = Route.ONBOARDING_PREFERENCES_FORM, finishCurrent = true)
        } catch (ex: MessageException) {
            errorDialog.value = ex
        } catch (ex: ConsistencyException) {
            outcome.value = ex.outcome
        } finally {
            showLoading.value = false
        }
    }
}
