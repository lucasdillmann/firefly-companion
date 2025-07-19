package br.com.dillmann.fireflycompanion.thirdparty.core.converter

import org.mapstruct.factory.Mappers

fun <T> getConverter(typeClass: Class<T>): T =
    Mappers.getMapper(typeClass)

inline fun <reified T> getConverter(): T =
    getConverter(T::class.java)
