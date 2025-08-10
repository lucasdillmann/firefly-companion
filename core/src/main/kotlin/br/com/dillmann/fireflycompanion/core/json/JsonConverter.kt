package br.com.dillmann.fireflycompanion.core.json

import kotlin.reflect.KClass

interface JsonConverter {
    fun <T : Any> parse(json: String, targetClass: KClass<T>): T
    fun <T : Any> toJson(content: T): String?
}
