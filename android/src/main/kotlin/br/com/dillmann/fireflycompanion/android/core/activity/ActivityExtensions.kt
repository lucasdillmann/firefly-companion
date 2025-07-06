package br.com.dillmann.fireflycompanion.android.core.activity

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Context> Context.start(
    finish: Boolean = false,
    replacePrevious: Boolean = false,
) {
    val intent = Intent(this, T::class.java)
    if (replacePrevious)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(intent)

    if (finish)
        (this as Activity).finish()
}
