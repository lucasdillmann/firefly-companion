package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.home.components.HomeMainTab
import br.com.dillmann.fireflycompanion.android.home.components.HomeMoreTab
import br.com.dillmann.fireflycompanion.android.home.components.HomeTransactionsTab

class HomeActivity : PreconfiguredActivity() {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content(padding: PaddingValues) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    HomeTabs.entries.forEach {
                        NavBarTab(it, navController, currentDestination)
                    }
                }
            },
            floatingActionButton = {
                QuickActions()
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = HomeTabs.MAIN.name,
                modifier = Modifier.padding(it)
            ) {
                composable(HomeTabs.MAIN.name) { HomeMainTab() }
                composable(HomeTabs.TRANSACTIONS.name) { HomeTransactionsTab() }
                composable(HomeTabs.MORE.name) { HomeMoreTab() }
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

@Composable
private fun QuickActions() {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SmallFloatingActionButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = i18n(R.string.ia_assistant),
                modifier = Modifier.size(16.dp),
            )
        }

        FloatingActionButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = i18n(R.string.new_transaction),
            )
        }
    }
}
