package br.com.dillmann.fireflycompanion.android.core.components.taginput

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TagOutlineTextField(
    tags: MutableState<Set<String>>,
    label: String,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    suggestionsProvider: suspend (query: String) -> List<String>,
) {
    val queue by persistent(ActionQueue())
    var expanded by volatile(false)
    var suggestions by volatile(emptyList<String>())
    var input by volatile(TextFieldValue(""))

    fun fetchSuggestions() {
        if (disabled)
            return

        queue.add {
            suggestions = suggestionsProvider(input.text).filterNot { tags.value.contains(it) }
            expanded = suggestions.isNotEmpty()
        }
    }

    fun commitInput() {
        if (disabled) return

        val text = input.text.trim()
        if (text.isNotEmpty() && tags.value.none { it.equals(text, ignoreCase = true) }) {
            tags.value += text
        }

        input = TextFieldValue("")
    }

    fun commitSuggestion(suggestion: String) {
        tags.value += suggestion
        input = TextFieldValue("")
        expanded = false
    }

    fun removeTag(tag: String) {
        if (disabled) return
        tags.value -= tag
    }

    fun handleBackspaceKey(event: KeyEvent): Boolean {
        if (event.key == Key.Backspace && input.text.isEmpty() && tags.value.isNotEmpty()) {
            tags.value -= tags.value.last()
            return true
        }

        return false
    }

    BackHandler(enabled = expanded) {
        expanded = false
    }

    ExposedDropdownMenuBox(
        expanded = expanded && !disabled,
        onExpandedChange = { expanded = it && !disabled },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                fetchSuggestions()
            },
            isError = isError,
            enabled = !disabled,
            singleLine = false,
            label = { Text(label) },
            supportingText = supportingText,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .onKeyEvent(::handleBackspaceKey),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { commitInput() },
            ),
            prefix = { TagList(tags, disabled, ::removeTag) },
        )

        ExposedDropdownMenu(
            expanded = expanded && !disabled,
            onDismissRequest = { expanded = false },
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion) },
                    onClick = { commitSuggestion(suggestion) },
                )
            }
        }
    }
}

@Composable
private fun TagList(
    tags: MutableState<Set<String>>,
    disabled: Boolean = false,
    removeTag: (String) -> Unit,
) {
    if (tags.value.isEmpty())
        return

    Column {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            tags.value.forEach { tag ->
                TagChip(tag, disabled, removeTag)
            }
        }
    }
}

@Composable
private fun TagChip(
    tag: String,
    disabled: Boolean = false,
    removeTag: (String) -> Unit,
) {
    AssistChip(
        onClick = {},
        label = { Text(tag) },
        trailingIcon = {
            if (!disabled) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(2.dp)
                        .clickable { removeTag(tag) },
                )
            }
        },
        enabled = !disabled,
        modifier = Modifier
            .padding(all = 0.dp)
            .padding(end = 2.dp),
    )
}
