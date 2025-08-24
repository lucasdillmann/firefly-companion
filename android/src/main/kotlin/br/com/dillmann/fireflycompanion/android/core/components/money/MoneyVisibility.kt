package br.com.dillmann.fireflycompanion.android.core.components.money

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import br.com.dillmann.fireflycompanion.business.currency.Currency
import java.math.BigDecimal
import kotlin.reflect.KProperty

object MoneyVisibility {
    private const val PREFERENCES_KEY = "money_visibility"
    private const val VALUE_KEY = "is_visible"
    private const val DEFAULT_VALUE = true

    private lateinit var preferences: SharedPreferences
    var state = mutableStateOf(DEFAULT_VALUE)
        private set

    fun init(context: Context) {
        preferences = context
            .applicationContext
            .getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

        state.value = preferences.getBoolean(VALUE_KEY, DEFAULT_VALUE)
    }

    fun toggle() {
        state.value = !state.value

        with(preferences.edit()) {
            putBoolean(VALUE_KEY, state.value)
            apply()
        }
    }

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): State<Boolean> {
        return state
    }

    fun format(value: BigDecimal, currency: Currency): String =
        if (state.value) currency.format(value)
        else "${currency.symbol} •••"
}
