package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.home.components.HomeAccountsTab
import br.com.dillmann.fireflycompanion.android.home.components.HomeMainTab
import br.com.dillmann.fireflycompanion.android.home.components.HomeQuickActions
import br.com.dillmann.fireflycompanion.android.home.components.HomeTransactionsTab

class HomeActivity : PreconfiguredActivity() {
    private companion object {
        private val QUICK_ACTION_ENABLED_TABS =
            listOf(HomeTabs.MAIN, HomeTabs.TRANSACTIONS).map(HomeTabs::name)
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content(padding: PaddingValues) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            bottomBar = {
                NavigationBar {
                    HomeTabs.entries.forEach {
                        NavBarTab(it, navController, currentDestination)
                    }
                }
            },
            floatingActionButton = {
                if (currentDestination?.route in QUICK_ACTION_ENABLED_TABS)
                    HomeQuickActions()
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeTabs.MAIN.name,
                modifier = Modifier.padding(it)
            ) {
                composable(HomeTabs.MAIN.name) { HomeMainTab() }
                composable(HomeTabs.TRANSACTIONS.name) { HomeTransactionsTab() }
                composable(HomeTabs.ACCOUNTS.name) { HomeAccountsTab() }
            }
        }
    }
}

@Composable
private fun RowScope.NavBarTab(
    tab: HomeTabs,
    controller: NavController,
    current: NavDestination?,
) {
    val description = i18n(tab.title)
    NavigationBarItem(
        selected = current?.hierarchy?.any { it.route == tab.name } == true,
        onClick = {
            controller.navigate(tab.name) {
                popUpTo(controller.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = { Icon(tab.icon, contentDescription = description) },
        label = { Text(description) }
    )
}
