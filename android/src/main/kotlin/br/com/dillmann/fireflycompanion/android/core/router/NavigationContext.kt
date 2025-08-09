package br.com.dillmann.fireflycompanion.android.core.router

import java.io.Serializable

data class NavigationContext(
    private val event: NavigationEvent,
) {
    fun currentRoute() =
        event.route

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
        if (finishCurrent) finish()
        if (replacePrevious) RouterState.stack.removeLastOrNull()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBagValue() =
        event.bag as? T?

    fun <T> requireBagValue() =
        getBagValue<T>()!!
}
