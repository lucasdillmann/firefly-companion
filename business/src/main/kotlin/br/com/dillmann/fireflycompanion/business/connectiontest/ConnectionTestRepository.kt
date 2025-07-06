package br.com.dillmann.fireflycompanion.business.connectiontest

interface ConnectionTestRepository {
    suspend fun isServerReachable(url: String, token: String): Boolean
}
