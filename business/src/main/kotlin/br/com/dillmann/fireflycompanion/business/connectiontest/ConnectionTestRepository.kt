package br.com.dillmann.fireflycompanion.business.connectiontest

fun interface ConnectionTestRepository {
    suspend fun isServerReachable(url: String, token: String): Boolean
}
