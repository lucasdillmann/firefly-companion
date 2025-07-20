package br.com.dillmann.fireflycompanion.android.core.components.autocomplete

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import br.com.dillmann.fireflycompanion.android.core.activity.async
import br.com.dillmann.fireflycompanion.android.core.activity.state
import br.com.dillmann.fireflycompanion.android.core.extensions.cancel
import java.util.concurrent.CompletableFuture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteOutlinedTextField(
    value: MutableState<TextFieldValue>,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    suggestionsProvider: suspend (query: String) -> List<String>,
) {
    var expanded by state(false)
    var suggestions by state(emptyList<String>())
    var searchJob by state<CompletableFuture<Unit>?>(value = null, persistent = false)
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
            expanded = showDropdown,
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
