package br.com.dillmann.fireflycompanion.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import br.com.dillmann.fireflycompanion.business.preferences.Preferences
import br.com.dillmann.fireflycompanion.business.preferences.usecase.GetPreferencesUseCase
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.getKoin

private val DarkColorScheme =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
    )

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val getPreferences = getKoin().get<GetPreferencesUseCase>()
    val preferences = runBlocking { getPreferences.getPreferences() }

    val colorScheme = when(preferences.theme) {
        Preferences.Theme.AUTO ->
            if (isSystemInDarkTheme()) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)

        Preferences.Theme.LIGHT -> LightColorScheme
        Preferences.Theme.DARK -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes(),
        content = content
    )
}
