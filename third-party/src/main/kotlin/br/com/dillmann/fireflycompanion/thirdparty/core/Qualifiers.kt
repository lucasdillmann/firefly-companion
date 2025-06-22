package br.com.dillmann.fireflycompanion.thirdparty.core

import org.koin.core.qualifier.qualifier

object Qualifiers {
    val API_BASE_URL = qualifier("firefly.client.baseUrl")
}
