package br.com.dillmann.fireflycompanion.android.core.router

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import br.com.dillmann.fireflycompanion.android.accounts.AccountForm
import br.com.dillmann.fireflycompanion.android.home.HomeScreen
import br.com.dillmann.fireflycompanion.android.onboarding.OnboardingPreferencesForm
import br.com.dillmann.fireflycompanion.android.onboarding.OnboardingServerForm
import br.com.dillmann.fireflycompanion.android.onboarding.OnboardingStartScreen
import br.com.dillmann.fireflycompanion.android.preferences.PreferencesForm
import br.com.dillmann.fireflycompanion.android.transaction.TransactionForm

enum class Route(val root: @Composable NavigationContext.() -> Unit) : NavKey {
    HOME_SCREEN(NavigationContext::HomeScreen),
    ACCOUNT_FORM(NavigationContext::AccountForm),
    ONBOARDING_PREFERENCES_FORM(NavigationContext::OnboardingPreferencesForm),
    ONBOARDING_SERVER_FORM(NavigationContext::OnboardingServerForm),
    ONBOARDING_START_SCREEN(NavigationContext::OnboardingStartScreen),
    PREFERENCES_FORM(NavigationContext::PreferencesForm),
    TRANSACTION_FORM(NavigationContext::TransactionForm),
}
