package br.com.dillmann.fireflycompanion.android.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.activity.start
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.home.tabs.HomeAccountsTab
import br.com.dillmann.fireflycompanion.android.home.tabs.HomeAssistantTab
import br.com.dillmann.fireflycompanion.android.home.tabs.HomeMainTab
import br.com.dillmann.fireflycompanion.android.home.tabs.HomeTransactionsTab
import br.com.dillmann.fireflycompanion.android.transaction.TransactionFormActivity

class HomeActivity : PreconfiguredActivity() {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content(padding: PaddingValues) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val context = LocalContext.current

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val currentTab = currentDestination?.route?.let { HomeTabs.valueOf(it) }
                        val requestCode = currentTab?.ordinal ?: -1
                        context.start<TransactionFormActivity>(requestCode = requestCode)
                    },
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 4.dp)
                        .size(56.dp)
                        .clip(RoundedCornerShape(56.dp)),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = i18n(R.string.new_transaction),
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = HomeTabs.MAIN.name,
                ) {
                    composable(HomeTabs.MAIN.name) { HomeMainTab(resultNotifier) }
                    composable(HomeTabs.TRANSACTIONS.name) { HomeTransactionsTab(resultNotifier) }
                    composable(HomeTabs.ACCOUNTS.name) { HomeAccountsTab(resultNotifier) }
                    composable(HomeTabs.ASSISTANT.name) { HomeAssistantTab() }
                }

                NavigationBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(end = 96.dp, start = 24.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(64.dp)),
                ) {
                    HomeTabs.entries.forEach {
                        NavBarTab(it, navController, currentDestination)
                    }
                }
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
    val selected = current?.route == tab.name
    val backgroundColor by animateColorAsState(
        targetValue =
            if (selected) MaterialTheme.colorScheme.secondaryContainer
            else Color.Transparent,
        label = "backgroundColorAnimation"
    )
    val iconColor by animateColorAsState(
        targetValue =
            if (selected) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "iconColorAnimation"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    controller.navigate(tab.name) {
                        popUpTo(controller.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = backgroundColor, shape = CircleShape)
                .clip(RoundedCornerShape(40.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = description,
                modifier = Modifier.size(24.dp),
                tint = iconColor,
            )
        }
    }
}
