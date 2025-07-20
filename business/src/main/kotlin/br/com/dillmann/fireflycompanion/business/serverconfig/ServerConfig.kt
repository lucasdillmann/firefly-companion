package br.com.dillmann.fireflycompanion.business.serverconfig

import java.io.Serializable

data class ServerConfig(
    val url: String,
    val token: String,
) : Serializable
