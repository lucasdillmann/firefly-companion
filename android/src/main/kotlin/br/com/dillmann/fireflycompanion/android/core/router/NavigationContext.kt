package br.com.dillmann.fireflycompanion.android.core.router

import java.io.Serializable
import kotlin.collections.removeLastOrNull

data class NavigationContext(
    private val event: NavigationEvent,
) {
    fun finish() {
        RouterState.stack -= event
    }

    fun open(
        route: Route,
        finishCurrent: Boolean = false,
        replacePrevious: Boolean = false,
        bag: Serializable? = null,
    ) {
        navigate(route, bag)

        if (finishCurrent)
            finish()

        if (replacePrevious)
            RouterState.stack.removeLastOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBagValue() =
        event.bag as? T?

    fun <T> requireBagValue() =
        getBagValue<T>()!!
}
