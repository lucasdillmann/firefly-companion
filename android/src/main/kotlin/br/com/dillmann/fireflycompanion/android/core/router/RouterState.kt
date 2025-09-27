package br.com.dillmann.fireflycompanion.android.core.router

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

object RouterState {
    lateinit var stack: NavBackStack<NavKey>
        private set

    fun init(stack: NavBackStack<NavKey>) {
        this.stack = stack
    }
}
