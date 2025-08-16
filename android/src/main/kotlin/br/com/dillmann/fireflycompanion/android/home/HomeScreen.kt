package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.dillmann.fireflycompanion.android.core.animations.Transitions
import br.com.dillmann.fireflycompanion.android.core.router.NavigationContext
import br.com.dillmann.fireflycompanion.android.home.accounts.HomeAccountsTab
import br.com.dillmann.fireflycompanion.android.home.assistant.HomeAssistantTab
import br.com.dillmann.fireflycompanion.android.home.main.HomeMainTab
import br.com.dillmann.fireflycompanion.android.home.transactions.HomeTransactionsTab

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NavigationContext.HomeScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(modifier = Modifier.padding(top = 32.dp)) {
        NavHost(
            navController = navController,
            startDestination = HomeTabs.MAIN.name,
            enterTransition = {
                val (initialIndex, targetIndex) = transitionIndexes()
                if (targetIndex > initialIndex) {
                    Transitions.pushEnter
                } else {
                    Transitions.popEnter
                }
            },
            exitTransition = {
                val (initialIndex, targetIndex) = transitionIndexes()
                if (targetIndex > initialIndex) {
                    Transitions.pushExit
                } else {
                    Transitions.popExit
                }
            },
        ) {
            composable(HomeTabs.MAIN.name) { HomeMainTab() }
            composable(HomeTabs.TRANSACTIONS.name) { HomeTransactionsTab() }
            composable(HomeTabs.ACCOUNTS.name) { HomeAccountsTab() }
            composable(HomeTabs.ASSISTANT.name) { HomeAssistantTab() }
        }

        FloatingActionBar(
            navController = navController,
            currentDestination = currentDestination,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.transitionIndexes(): Pair<Int, Int> {
    val initialRoute = initialState.destination.route
    val targetRoute = targetState.destination.route
    val initialTabIndex = HomeTabs.entries.indexOfFirst { it.name == initialRoute }
    val targetTabIndex = HomeTabs.entries.indexOfFirst { it.name == targetRoute }

    return initialTabIndex to targetTabIndex
}


