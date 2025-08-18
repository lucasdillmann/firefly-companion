package br.com.dillmann.fireflycompanion.core.json

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import kotlin.reflect.KClass

internal class GsonJsonConverter : JsonConverter {
    private val delegate = Converters.registerAll(GsonBuilder()).create()

    override fun <T : Any> parse(json: String, targetClass: KClass<T>): T =
        delegate.fromJson(json, targetClass.java)

    override fun <T : Any> toJson(content: T): String =
        delegate.toJson(content)
}
