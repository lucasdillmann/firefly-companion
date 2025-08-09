package br.com.dillmann.fireflycompanion.android.core.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset

object Transitions {
    private val animationSpec =
        tween<IntOffset>(durationMillis = 300)

    val pushAnimation =
        slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) +
            fadeIn(animationSpec = tween(300, 90)) togetherWith
            slideOutHorizontally(targetOffsetX = { -it / 4 }, animationSpec = animationSpec) +
            fadeOut(animationSpec = tween(300))

    val popAnimation =
        slideInHorizontally(initialOffsetX = { -it / 4 }, animationSpec = animationSpec) +
            fadeIn(animationSpec = tween(300)) togetherWith
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) +
            fadeOut(animationSpec = tween(300))

    val pushEnter =
        slideInHorizontally(initialOffsetX = { it }, animationSpec = animationSpec) +
            fadeIn(animationSpec = tween(300, 90))

    val pushExit =
        slideOutHorizontally(targetOffsetX = { -it / 4 }, animationSpec = animationSpec) +
            fadeOut(animationSpec = tween(300))

    val popEnter =
        slideInHorizontally(initialOffsetX = { -it / 4 }, animationSpec = animationSpec) +
            fadeIn(animationSpec = tween(300))

    val popExit =
        slideOutHorizontally(targetOffsetX = { it }, animationSpec = animationSpec) +
            fadeOut(animationSpec = tween(300))
}
