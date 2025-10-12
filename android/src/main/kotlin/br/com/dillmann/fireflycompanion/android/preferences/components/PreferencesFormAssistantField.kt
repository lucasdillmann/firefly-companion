package br.com.dillmann.fireflycompanion.android.preferences.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppTextField
import br.com.dillmann.fireflycompanion.android.core.compose.async
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.extensions.description
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.koin.get
import br.com.dillmann.fireflycompanion.business.assistant.usecase.GetAvailableModelsUseCase
import br.com.dillmann.fireflycompanion.business.preferences.Preferences

@Composable
fun PreferencesFormAssistantField(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    val preferences by state
    val assistant = preferences.assistant

    ProviderSelector(state, onChange)

    if (assistant.provider == Preferences.AssistantProvider.OPEN_AI_COMPATIBLE) {
        PreferencesFormSpacer()
        ApiUrl(state, onChange)
    }

    if (assistant.provider != Preferences.AssistantProvider.DISABLED) {
        PreferencesFormSpacer()
        AccessToken(state, onChange)

        PreferencesFormSpacer()
        ModelSelector(state, onChange)
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
    val preferences by state
    val assistant = preferences.assistant

    val missingRequirements = assistant.accessToken.isNullOrBlank() ||
        (assistant.provider == Preferences.AssistantProvider.OPEN_AI_COMPATIBLE && assistant.url.isNullOrBlank())

    var expanded by volatile(false)
    var models by volatile(emptyList<String>())

    LaunchedEffect("${assistant.provider}${assistant.url}") {
        models = emptyList()

        if (missingRequirements)
            return@LaunchedEffect

        async {
            models = get<GetAvailableModelsUseCase>().getAvailableModels()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = i18n(R.string.assistant_model),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )

        Box {
            val currentText =
                if (missingRequirements) i18n(R.string.assistant_model_fill_required_fields)
                else assistant.model ?: models.firstOrNull() ?: i18n(R.string.loading)

            Button(
                onClick = { if (!missingRequirements && models.isNotEmpty()) expanded = !expanded },
                enabled = !missingRequirements && models.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                content = { Text(text = currentText) },
            )

            if (!missingRequirements) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    models.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(text = model) },
                            onClick = {
                                val updatedAssistant = assistant.copy(model = model)
                                state.value = preferences.copy(assistant = updatedAssistant)
                                onChange()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccessToken(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    var preferences by state
    var value by volatile(TextFieldValue(preferences.assistant.accessToken ?: ""))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = i18n(R.string.assistant_access_key),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )

        AppTextField(
            value = value,
            onChange = { newValue ->
                value = newValue

                val updated = preferences.assistant.copy(accessToken = newValue.text.ifBlank { null })
                preferences = preferences.copy(assistant = updated)

                onChange()
            },
            containerModifier = Modifier.weight(2f),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
            ),
            singleLine = true,
        )
    }
}

@Composable
private fun ApiUrl(
    state: MutableState<Preferences>,
    onChange: () -> Unit,
) {
    var preferences by state
    var value by volatile(TextFieldValue(preferences.assistant.url ?: ""))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = i18n(R.string.server_url),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )

        AppTextField(
            value = value,
            onChange = { newValue ->
                value = newValue

                val updated = preferences.assistant.copy(url = newValue.text.ifBlank { null })
                preferences = preferences.copy(assistant = updated)

                onChange()
            },
            label = i18n(R.string.server_url),
            modifier = Modifier.weight(2f),
            singleLine = true,
        )
    }
}
