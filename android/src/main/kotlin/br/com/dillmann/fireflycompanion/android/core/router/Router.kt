package br.com.dillmann.fireflycompanion.android.core.router

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.dillmann.fireflycompanion.android.core.components.animations.Transitions

@Composable
fun Router(initialRoute: Route) {
    val initialEvent = NavigationEvent(initialRoute)
    RouterState.init(rememberNavBackStack(initialEvent))

    NavDisplay(
        backStack = RouterState.stack,
        onBack = { RouterState.stack.removeLastOrNull() },
        popTransitionSpec = { Transitions.popAnimation },
        transitionSpec = { Transitions.pushAnimation },
        entryProvider = { key ->
            if (key !is NavigationEvent)
                error("Invalid key type: ${key::class.simpleName}")

            NavEntry(key, key.route) {
                val root = key.route.root
                NavigationContext(key).root()
            }
        }
    )
}
