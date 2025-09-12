package br.com.dillmann.fireflycompanion.android.home.assistant

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.components.textfield.AppTextField
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession
import br.com.dillmann.fireflycompanion.business.assistant.AssistantSession.State
import br.com.dillmann.fireflycompanion.business.assistant.model.AssistantMessage
import br.com.dillmann.fireflycompanion.business.assistant.usecase.StartAssistantSessionUseCase
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAssistantTab() {
    val useCase = getKoin().get<StartAssistantSessionUseCase>()
    var messages by persistent(emptyList<AssistantMessage>())
    val session by volatile { useCase.startSession(AppContext.currentLocale().toLanguageTag()) }

    val queue by persistent(ActionQueue())
    var input by volatile(TextFieldValue(""))
    var state by persistent(State.IDLE)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (!messages.isEmpty())
            return@LaunchedEffect

        messages += listOf(
            AssistantMessage(
                timestamp = OffsetDateTime.now(),
                sender = AssistantMessage.Sender.ASSISTANT,
                content = i18n(R.string.assistant_initial_message),
            )
        )
    }

    fun appendMessage(message: AssistantMessage, forceScroll: Boolean = false) =
        scope.launch {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val wasAtBottom = lastVisible >= (messages.size - 1)

            messages += message

            if (wasAtBottom || forceScroll) {
                listState.animateScrollBy(listState.layoutInfo.viewportSize.height.toFloat())
            }
        }

    val callback = object : AssistantSession.Callback {
        override suspend fun message(response: AssistantMessage) {
            appendMessage(response)
        }

        override suspend fun state(newState: State) {
            state = newState
        }
    }

    fun sendMessageIfAble() {
        val canSend = input.text.isNotBlank() && state == State.IDLE
        if (!canSend) return

        val content = input.text.trim()
        input = TextFieldValue("")
        appendMessage(
            message = AssistantMessage(
                timestamp = OffsetDateTime.now(),
                sender = AssistantMessage.Sender.USER,
                content = content,
            ),
            forceScroll = true,
        )

        queue.add {
            try {
                session.sendMessage(content, callback)
            } catch (ex: Exception) {
                Log.w("HomeAssistantTab", "Error sending assistant message", ex)
                appendMessage(
                    AssistantMessage(
                        timestamp = OffsetDateTime.now(),
                        sender = AssistantMessage.Sender.ASSISTANT,
                        content = i18n(R.string.assistant_generic_error_response),
                    )
                )
                state = State.IDLE
            }
        }
    }

    Section(
        title = i18n(R.string.assistant_title),
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                state = listState,
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                itemsIndexed(messages) { _, msg ->
                    MessageBubble(message = msg)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (state != State.IDLE) {
                    item {
                        ThinkingBubble(state)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(50.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                AppTextField(
                    value = input,
                    onChange = { input = it },
                    label = i18n(R.string.assistant_input_hint),
                    singleLine = true,
                    stickyLabel = false,
                    containerModifier = Modifier
                        .weight(2f)
                        .padding(end = 8.dp),
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { sendMessageIfAble() }
                    ),
                )

                val canSend = input.text.isNotBlank() && state == State.IDLE
                Button(
                    modifier = Modifier.size(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = canSend,
                    onClick = ::sendMessageIfAble,
                ) {
                    Icon(
                        contentDescription = i18n(R.string.assistant_send),
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: AssistantMessage) {
    val isUser = message.sender == AssistantMessage.Sender.USER
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val containerColor =
        if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor =
        if (isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp,
                    )
                )
                .background(containerColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 320.dp),
        ) {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(color = contentColor)) {
                Text(text = message.content)
            }
        }
        val timestamp = remember(message.timestamp) {
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(message.timestamp)
        }
        Text(
            text = timestamp,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun ThinkingBubble(state: State) {
    val label =
        if (state == State.GATHERING_DATA) i18n(R.string.assistant_gathering_data)
        else i18n(R.string.assistant_thinking)

    val transition = rememberInfiniteTransition(label = label)
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val dots = when {
        phase < 0.25f -> ""
        phase < 0.5f -> "."
        phase < 0.75f -> ".."
        else -> "..."
    }

    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 16.dp,
                    )
                )
                .background(containerColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 320.dp)
                .animateContentSize(
                    animationSpec = tween(150, easing = FastOutSlowInEasing)
                ),
        ) {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(color = contentColor)) {
                Text(text = label + dots)
            }
        }
    }
}
