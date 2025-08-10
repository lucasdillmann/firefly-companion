package br.com.dillmann.fireflycompanion.core.json

inline fun <reified T : Any> JsonConverter.parse(json: String): T =
    parse(json, T::class)
