package br.com.dillmann.fireflycompanion.android.core.router

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class NavigationEvent(
    val route: Route,
    val bag: java.io.Serializable? = null,
) : NavKey
