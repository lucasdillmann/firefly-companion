package br.com.dillmann.fireflycompanion.android.core.components.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

object Transitions {
    private const val ANIMATION_DURATION_MILLIS = 350

    val offsetSpec =
        tween<IntOffset>(durationMillis = ANIMATION_DURATION_MILLIS)

    val floatSpec =
        tween<Float>(durationMillis = ANIMATION_DURATION_MILLIS)

    val intSizeSpec =
        tween<IntSize>(durationMillis = ANIMATION_DURATION_MILLIS)

    val pushAnimation =
        slideInHorizontally(initialOffsetX = { it }, animationSpec = offsetSpec) +
            fadeIn(animationSpec = floatSpec) togetherWith
            slideOutHorizontally(targetOffsetX = { -it / 4 }, animationSpec = offsetSpec) +
            fadeOut(animationSpec = floatSpec)

    val popAnimation =
        slideInHorizontally(initialOffsetX = { -it / 4 }, animationSpec = offsetSpec) +
            fadeIn(animationSpec = floatSpec) togetherWith
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = offsetSpec) +
            fadeOut(animationSpec = floatSpec)

    val pushEnter =
        slideInHorizontally(initialOffsetX = { it }, animationSpec = offsetSpec) +
            fadeIn(animationSpec = floatSpec)

    val pushExit =
        slideOutHorizontally(targetOffsetX = { -it / 4 }, animationSpec = offsetSpec) +
            fadeOut(animationSpec = floatSpec)

    val popEnter =
        slideInHorizontally(initialOffsetX = { -it / 4 }, animationSpec = offsetSpec) +
            fadeIn(animationSpec = floatSpec)

    val popExit =
        slideOutHorizontally(targetOffsetX = { it }, animationSpec = offsetSpec) +
            fadeOut(animationSpec = floatSpec)

    fun <T> content(): AnimatedContentTransitionScope<T>.() -> ContentTransform =
        {
            fadeIn(animationSpec = floatSpec) togetherWith fadeOut(animationSpec = floatSpec)
        }
}
