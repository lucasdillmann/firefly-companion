package br.com.dillmann.fireflycompanion.android.home.transactions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun HomeTransactionsSearchField(
    searchTerms: String,
    enabled: Boolean,
    onChange: (String) -> Unit,
    refresh: () -> Unit,
) {
    TextField(
        value = searchTerms,
        onValueChange = { onChange(it.replace("\n", "")) },
        label = { Text(text = i18n(R.string.search)) },
        enabled = enabled,
        modifier = Modifier
            .padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            Button(
                onClick = refresh,
                enabled = enabled,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = i18n(R.string.search),
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { refresh() },
            onDone = { refresh() },
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
    )
}
