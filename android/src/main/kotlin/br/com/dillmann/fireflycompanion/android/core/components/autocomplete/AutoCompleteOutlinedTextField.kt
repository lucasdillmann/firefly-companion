package br.com.dillmann.fireflycompanion.android.core.components.autocomplete

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.emptyVolatile
import br.com.dillmann.fireflycompanion.android.core.activity.persistent
import br.com.dillmann.fireflycompanion.android.core.extensions.cancel
import java.util.concurrent.CompletableFuture

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
    var expanded by persistent(false)
    var suggestions by persistent(emptyList<String>())
    var searchJob by emptyVolatile<CompletableFuture<Unit>>()
    val showDropdown = suggestions.isNotEmpty() && expanded

    fun fetchSuggestions() {
        searchJob.cancel()
        searchJob = async {
            suggestions = suggestionsProvider(value.value.text)
        }
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = showDropdown,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = value.value,
            onValueChange = {
                value.value = it
                fetchSuggestions()
                expanded = true
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
                        fetchSuggestions()
                        expanded = true
                    }
                },
        )

        ExposedDropdownMenu(
            expanded = showDropdown && !disabled,
            onDismissRequest = { expanded = false },
        ) {
            suggestions.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        value.value = TextFieldValue(it)
                        searchJob.cancel()
                        expanded = false
                    }
                )
            }
        }
    }
}
