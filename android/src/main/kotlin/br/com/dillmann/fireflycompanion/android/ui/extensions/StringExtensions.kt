package br.com.dillmann.fireflycompanion.android.ui.extensions

fun String.noLineBreaks() =
    trimIndent().replace("\n", " ")
