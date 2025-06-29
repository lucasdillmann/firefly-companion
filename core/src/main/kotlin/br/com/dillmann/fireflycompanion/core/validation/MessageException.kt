package br.com.dillmann.fireflycompanion.core.validation

class MessageException(
    val title: String,
    message: String,
) : ValidationException(message)
