package br.com.dillmann.fireflycompanion.android.core.router

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavBackStack

object RouterState {
    lateinit var stack: NavBackStack
        private set

    fun init(stack: NavBackStack) {
        this.stack = stack
    }

    @Suppress("UNCHECKED_CAST")
    fun typedStack() =
        stack as SnapshotStateList<NavigationEvent>
}
