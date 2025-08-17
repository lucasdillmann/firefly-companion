package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.extensions.description
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.business.assistant.usecase.GetAvailableModelsUseCase
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun PreferencesFormAssistantField(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    val preferences by state
    val assistant = preferences.assistant

    ProviderSelector(state, onChange)

    if (assistant.provider != Preferences.AssistantProvider.DISABLED) {
        PreferencesFormSpacer()
        ModelSelector(state, onChange)
        PreferencesFormSpacer()
        AccessToken(state, onChange)
    }

    if (assistant.provider == Preferences.AssistantProvider.OPEN_AI_COMPATIBLE) {
        PreferencesFormSpacer()
        ApiUrl(state, onChange)
    }
}

@Composable
private fun ProviderSelector(
    state: MutableState<Preferences>,
    onChange: () -> Unit
) {
    var expanded by volatile(false)
    val preferences by state
    var assistant = preferences.assistant

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = i18n(R.string.assistant_provider),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )

        Box {
            Button(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                content = { Text(text = assistant.provider.description()) },
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                Preferences.AssistantProvider.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.description()) },
                        onClick = {
                            assistant = assistant.copy(provider = option)
                            state.value = preferences.copy(assistant = assistant)
                            onChange()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ModelSelector(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    val assistant = state.value.assistant
    if (assistant.accessToken.isNullOrBlank()) {
        // TODO: Disabled selector telling to fill the token first
        return
    }

    if (assistant.provider == Preferences.AssistantProvider.OPEN_AI_COMPATIBLE && assistant.url.isNullOrBlank()) {
        // TODO: Disabled selector telling to fill the url first
        return
    }

    val useCase = getKoin().get<GetAvailableModelsUseCase>()
    // TODO: Selector with values from the use case
}

@Composable
private fun AccessToken(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    // TODO: Access token input field
}

@Composable
private fun ApiUrl(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    // TODO: Api url input field
}
