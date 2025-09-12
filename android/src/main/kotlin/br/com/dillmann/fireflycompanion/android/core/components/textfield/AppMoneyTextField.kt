package br.com.dillmann.fireflycompanion.android.core.components.textfield

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@Composable
fun AppMoneyTextField(
    value: BigDecimal,
    onChange: (BigDecimal) -> Unit,
    currency: Currency,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier.padding(vertical = 8.dp),
    textStyle: TextStyle = AppTextFieldDefaults.textStyle,
    label: String? = null,
    stickyLabel: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    errorMessage: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var textFieldValue by volatile(TextFieldValue())
    val decimalSeparator = DecimalFormatSymbols.getInstance(AppContext.currentLocale()).decimalSeparator

    LaunchedEffect(Unit) {
        if (!textFieldValue.text.isEmpty())
            return@LaunchedEffect

        val formattedValue = format(value, currency, decimalSeparator)
        textFieldValue = TextFieldValue(formattedValue)
    }

    AppTextField(
        value = textFieldValue,
        onChange = { newValue ->
            val (text, value) = handleInput(newValue.text, currency, decimalSeparator)
            val selection =
                if (text != newValue.text) TextRange(text.length)
                else newValue.selection

            textFieldValue = TextFieldValue(text, selection)
            onChange(value)
        },
        modifier = modifier,
        containerModifier = containerModifier,
        textStyle = textStyle,
        label = label,
        stickyLabel = stickyLabel,
        enabled = enabled,
        readOnly = readOnly,
        errorMessage = errorMessage,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
    )
}

private fun handleInput(
    input: String,
    currency: Currency,
    decimalSeparator: Char
): Pair<String, BigDecimal> {
    val decimalPlaces = currency.decimalPlaces
    val symbol = currency.symbol
    val pureValue = input.removePrefix(symbol).trim()
    val minusCount = pureValue.count { it == '-' }
    val negativePrefix = if (minusCount % 2 == 1) "-" else ""
    val digitsOnly = pureValue.filter { it.isDigit() }

    if (digitsOnly.isEmpty())
        return "$symbol ${negativePrefix}0$decimalSeparator${"0".repeat(decimalPlaces)}" to BigDecimal.ZERO

    if (digitsOnly.length == 1) {
        val formattedValue = "$symbol $negativePrefix$digitsOnly"
        val value = BigDecimal("$negativePrefix$digitsOnly").divide(BigDecimal.TEN.pow(decimalPlaces))
        return formattedValue to value
    }

    val wholePart = digitsOnly.dropLast(decimalPlaces).trimStart('0')
    val fractionalPart = digitsOnly.takeLast(decimalPlaces).padStart(decimalPlaces, '0')
    val wholePartDisplay = wholePart.ifEmpty { "0" }
    val formattedText = "$symbol $negativePrefix$wholePartDisplay$decimalSeparator$fractionalPart"
    val bigDecimalValue =
        runCatching {
            val fullNumber = (wholePart.ifEmpty { "0" }) + fractionalPart
            BigDecimal("$negativePrefix$fullNumber").divide(BigDecimal.TEN.pow(decimalPlaces))
        }.getOrElse {
            BigDecimal.ZERO
        }

    return formattedText to bigDecimalValue
}

private fun format(
    value: BigDecimal,
    currency: Currency,
    decimalSeparator: Char
): String {
    val isNegative = value.signum() < 0
    val absoluteValue = value.abs()

    val formatter = DecimalFormat("#.${"0".repeat(currency.decimalPlaces)}")
    val symbols = DecimalFormatSymbols.getInstance()
    symbols.decimalSeparator = decimalSeparator
    formatter.decimalFormatSymbols = symbols

    val negativePrefix = if (isNegative) "-" else ""
    val baseValue = formatter.format(absoluteValue)
    val formattedValue =
        if (baseValue.startsWith(decimalSeparator)) "0$baseValue"
        else baseValue

    return "${currency.symbol} $negativePrefix$formattedValue"
}
