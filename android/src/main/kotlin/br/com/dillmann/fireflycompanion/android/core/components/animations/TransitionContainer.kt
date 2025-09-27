package br.com.dillmann.fireflycompanion.android.core.components.animations

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@SuppressLint("UnusedContentLambdaTargetStateParameter")
fun TransitionContainer(
    state: Any?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = Transitions.content(),
        modifier = modifier,
    ) {
        content()
    }
}
