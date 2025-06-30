package br.com.dillmann.fireflycompanion.android.onboarding.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome

@Composable
fun OnboardingServerFormFields(
    scrollState: ScrollState,
    serverUrl: MutableState<TextFieldValue>,
    accessToken: MutableState<TextFieldValue>,
    validationOutcome: ValidationOutcome?,
) {
    val serverUrlFocusRequester = remember { FocusRequester() }
    val accessTokenFocusRequester = remember { FocusRequester() }
    val serverUrlFocused = remember { mutableStateOf(false) }
    val accessTokenFocused = remember { mutableStateOf(false) }

    LaunchedEffect(serverUrlFocused.value, accessTokenFocused.value) {
        if (serverUrlFocused.value) {
            scrollState.animateScrollTo(scrollState.value + 400)
        } else if (accessTokenFocused.value) {
            scrollState.animateScrollTo(scrollState.value + 500)
        }
    }

    OutlinedTextField(
        value = serverUrl.value,
        onValueChange = { serverUrl.value = it },
        label = { Text(text = "Server URL") },
        modifier = Modifier
            .padding(top = 32.dp, bottom = 8.dp)
            .focusRequester(serverUrlFocusRequester)
            .onFocusChanged { serverUrlFocused.value = it.isFocused }
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        isError = validationOutcome?.messageFor("url") != null,
        supportingText = {
            val message = validationOutcome?.messageFor("url")
            if (message != null) {
                Text(
                    text = message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    )

    OutlinedTextField(
        value = accessToken.value,
        onValueChange = { accessToken.value = it },
        label = { Text(text = "Personal access token") },
        modifier = Modifier
            .padding(top = 8.dp, bottom = 32.dp)
            .focusRequester(accessTokenFocusRequester)
            .onFocusChanged { accessTokenFocused.value = it.isFocused }
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
        ),
        isError = validationOutcome?.messageFor("token") != null,
        supportingText = {
            val message = validationOutcome?.messageFor("token")
            if (message != null) {
                Text(
                    text = message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    )
}
