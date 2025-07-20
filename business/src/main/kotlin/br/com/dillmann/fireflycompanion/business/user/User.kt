package br.com.dillmann.fireflycompanion.business.user

import java.io.Serializable

data class User(
    val id: String,
    val name: String,
) : Serializable
