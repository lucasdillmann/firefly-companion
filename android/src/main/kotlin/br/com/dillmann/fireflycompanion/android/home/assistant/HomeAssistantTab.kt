package br.com.dillmann.fireflycompanion.android.home.assistant

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.dillmann.fireflycompanion.android.R
import br.com.dillmann.fireflycompanion.android.core.components.section.Section
import br.com.dillmann.fireflycompanion.android.core.compose.persistent
import br.com.dillmann.fireflycompanion.android.core.compose.volatile
import br.com.dillmann.fireflycompanion.android.core.context.AppContext
import br.com.dillmann.fireflycompanion.android.core.i18n.i18n
import br.com.dillmann.fireflycompanion.android.core.queue.ActionQueue
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
    val session by volatile {
        val language = AppContext.resolve().resources.configuration.locales[0].toLanguageTag()
        useCase.startSession(language)
    }

    val queue by persistent(ActionQueue())
    var input by volatile(TextFieldValue(""))
    var thinking by persistent(false)
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

    fun appendMessages(newMessages: List<AssistantMessage>, forceScroll: Boolean = false) =
        scope.launch {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val wasAtBottom = lastVisible >= (messages.size - 1)

            messages += newMessages

            if (wasAtBottom || forceScroll) {
                listState.animateScrollBy(listState.layoutInfo.viewportSize.height.toFloat())
            }
        }

    fun appendMessage(newMessage: AssistantMessage, forceScroll: Boolean = false) =
        appendMessages(listOf(newMessage), forceScroll)

    fun sendMessageIfAble() {
        val canSend = input.text.isNotBlank() && !thinking
        if (!canSend) return
        val content = input.text.trim()
        input = TextFieldValue("")
        appendMessage(
            newMessage = AssistantMessage(
                timestamp = OffsetDateTime.now(),
                sender = AssistantMessage.Sender.USER,
                content = content,
            ),
            forceScroll = true,
        )
        thinking = true
        queue.add {
            try {
                val replies = session.sendMessage(content)
                appendMessages(replies)
            } catch (ex: Exception) {
                Log.w("HomeAssistantTab", "Error sending assistant message", ex)
                appendMessage(
                    AssistantMessage(
                        timestamp = OffsetDateTime.now(),
                        sender = AssistantMessage.Sender.ASSISTANT,
                        content = i18n(R.string.assistant_generic_error_response),
                    )
                )
            } finally {
                thinking = false
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
                if (thinking) {
                    item {
                        ThinkingBubble()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(50.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    placeholder = {
                        Text(
                            text = i18n(R.string.assistant_input_hint),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .height(50.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = { sendMessageIfAble() }
                    ),
                )

                Spacer(modifier = Modifier.width(8.dp))
                val canSend = input.text.isNotBlank() && !thinking

                Button(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = canSend,
                    onClick = { sendMessageIfAble() },
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
private fun ThinkingBubble() {
    val transition = rememberInfiniteTransition(label = "thinking")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )
    val dots = when {
        phase < 0.33f -> ""
        phase < 0.66f -> "."
        else -> ".."
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
                .widthIn(max = 320.dp),
        ) {
            ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(color = contentColor)) {
                Text(text = i18n(R.string.assistant_thinking) + dots)
            }
        }
    }
}
