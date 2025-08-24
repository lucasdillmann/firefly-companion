package br.com.dillmann.fireflycompanion.android.home.transactions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppTextField
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n

@Composable
fun HomeTransactionsSearchField(
    searchTerms: String,
    enabled: Boolean,
    onChange: (String) -> Unit,
    refresh: () -> Unit,
) {
    val height = 48.dp

    Row {
        AppTextField(
            value = TextFieldValue(searchTerms),
            onChange = { onChange(it.text.replace("\n", "")) },
            label = i18n(R.string.search),
            enabled = enabled,
            stickyLabel = false,
            containerModifier = Modifier
                .weight(2f)
                .padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 8.dp),
            modifier = Modifier
                .height(height)
                .padding(0.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodySmall,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = { refresh() },
                onDone = { refresh() },
            ),
            singleLine = true,
        )

        Button(
            modifier = Modifier.size(height),
            contentPadding = PaddingValues(0.dp),
            enabled = enabled,
            onClick = refresh,
        ) {
            Icon(
                contentDescription = i18n(R.string.search),
                imageVector = Icons.Default.Search,
                modifier = Modifier.size(height / 2),
            )
        }
    }
}
