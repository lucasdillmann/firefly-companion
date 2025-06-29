package br.com.dillmann.fireflycompanion.android.ui.activity

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

fun PreconfiguredActivity.async(action: suspend () -> Unit) {
    thread(start = true) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                action()
            }
        }
    }
}
