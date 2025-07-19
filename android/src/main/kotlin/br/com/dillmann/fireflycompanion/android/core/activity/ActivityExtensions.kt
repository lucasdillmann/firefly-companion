package br.com.dillmann.fireflycompanion.android.core.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import java.io.Serializable

inline fun <reified T : Context> Context.start(
    finish: Boolean = false,
    replacePrevious: Boolean = false,
    extras: Map<String, Serializable> = emptyMap(),
    requestCode: Int = -1,
) {
    val intent = Intent(this, T::class.java)
    val bundle = Bundle()

    if (replacePrevious)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    if (extras.isNotEmpty()) {
        extras.forEach { (key, value) -> bundle.putSerializable(key, value) }
        intent.putExtras(bundle)
    }

    startActivityForResult(this as Activity, intent, requestCode, bundle)

    if (finish)
        finish()
}
