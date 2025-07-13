package br.com.dillmann.fireflycompanion.android.core.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.io.Serializable

inline fun <reified T : Context> Context.start(
    finish: Boolean = false,
    replacePrevious: Boolean = false,
    extras: Map<String, Serializable> = emptyMap(),
) {
    val intent = Intent(this, T::class.java)
    if (replacePrevious)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    if (extras.isNotEmpty()) {
        val bundle = Bundle()
        extras.forEach { (key, value) -> bundle.putSerializable(key, value) }
        intent.putExtras(bundle)
    }

    startActivity(intent)

    if (finish)
        (this as Activity).finish()
}
