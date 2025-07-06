package br.com.dillmann.fireflycompanion.android.core.extensions

fun String.noLineBreaks() =
    trimIndent().replace("\n", " ")
