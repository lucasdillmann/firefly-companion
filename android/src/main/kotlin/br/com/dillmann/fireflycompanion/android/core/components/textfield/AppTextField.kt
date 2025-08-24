package br.com.dillmann.fireflycompanion.android.core.components.textfield

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import br.com.dillmann.fireflycompanion.android.core.theme.AppTextFieldDefaults

@Composable
fun AppTextField(
    value: TextFieldValue,
    onChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier.padding(vertical = 8.dp),
    textStyle: TextStyle = AppTextFieldDefaults.textStyle,
    label: String? = null,
    stickyLabel: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    prefix: @Composable (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(percent = 50)

    Box(modifier = containerModifier) {
        TextField(
            value = value,
            onValueChange = onChange,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            colors = AppTextFieldDefaults.colors,
            textStyle = textStyle,
            isError = errorMessage != null,
            supportingText = errorMessage.asText(color = Color.Red),
            placeholder = label.asText(style = textStyle),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            prefix = prefix,
            visualTransformation = visualTransformation,
            modifier = modifier
                .clip(shape)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = shape,
                ),
        )

        if (stickyLabel && label != null) {
            Label(
                label = label,
                visible = value.text.isNotBlank(),
            )
        }
    }
}

@Composable
private fun BoxScope.Label(
    label: String,
    visible: Boolean,
) {
    val baseColor = MaterialTheme.colorScheme.onSurfaceVariant
    val shape = RoundedCornerShape(percent = 50)
    val duration = if (visible) 300 else 0

    val offsetY by animateDpAsState(
        targetValue = if (visible) (-8).dp else 16.dp,
        animationSpec = tween(durationMillis = duration),
        label = "labelOffsetY"
    )

    val offsetX by animateDpAsState(
        targetValue = if (visible) (-4).dp else 16.dp,
        animationSpec = tween(durationMillis = duration),
        label = "labelOffsetX"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = duration),
        label = "labelAlpha"
    )

    Box(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = offsetX, y = offsetY)
            .alpha(alpha)
            .zIndex(1f)
            .background(
                color = baseColor,
                shape = shape,
            ),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun String?.asText(
    color: Color = Color.Unspecified,
    style: TextStyle = AppTextFieldDefaults.textStyle,
): @Composable (() -> Unit)? {
    if (this == null)
        return null

    return {
        Text(
            text = this,
            color = color,
            style = style,
        )
    }
}
