package br.com.dillmann.fireflycompanion.core.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.KClass

internal class MoshiJsonConverter : JsonConverter {
    private val delegate = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    override fun <T : Any> parse(json: String, targetClass: KClass<T>): T =
        delegate.adapter(targetClass.java).fromJson(json)!!

    override fun <T : Any> toJson(content: T): String {
        val adapter = delegate.adapter(content::class.java) as JsonAdapter<T>
        return adapter.toJson(content)
    }
}
