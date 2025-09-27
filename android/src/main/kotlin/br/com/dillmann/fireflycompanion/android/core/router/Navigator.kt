package br.com.dillmann.fireflycompanion.android.core.router

import java.io.Serializable

fun navigate(route: Route, bag: Serializable? = null) {
    RouterState.stack + NavigationEvent(route, bag)
}
