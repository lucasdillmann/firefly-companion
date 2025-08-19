package br.com.dillmann.fireflycompanion.android.core.components.autocomplete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteOutlinedTextField(
    value: MutableState<TextFieldValue>,
    label: String,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    suggestionsProvider: suspend (query: String) -> List<String>,
) {
    val queue by persistent(ActionQueue())
    var expanded by persistent(false)
    var suggestions by persistent(emptyList<String>())

    suspend fun fetchSuggestions() {
        if (disabled) return

        suggestions = suggestionsProvider(value.value.text)
        expanded = suggestions.isNotEmpty()
    }

    BackHandler(enabled = expanded) {
        expanded = false
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = value.value,
            onValueChange = {
                value.value = it
                queue.add { fetchSuggestions() }
            },
            isError = isError,
            label = { Text(label) },
            enabled = !disabled,
            supportingText = supportingText,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable)
                .onFocusChanged {
                    if (it.isFocused) {
                        queue.add { fetchSuggestions() }
                    }
                },
        )

        ExposedDropdownMenu(
            expanded = expanded && !disabled,
            onDismissRequest = { expanded = false },
        ) {
            suggestions.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        value.value = TextFieldValue(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
