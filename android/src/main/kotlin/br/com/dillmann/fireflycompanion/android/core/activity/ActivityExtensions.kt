package br.com.dillmann.fireflycompanion.android.core.activity

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Context> Context.start(finish: Boolean = false) {
    startActivity(Intent(this, T::class.java))

    if (finish) {
        (this as Activity).finish()
    }
}
