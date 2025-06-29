package br.com.dillmann.fireflycompanion.business.connectiontest

internal class ConnectionTestService(private val repository: ConnectionTestRepository) {
    suspend fun isServerReachable(url: String, token: String) =
        repository.isServerReachable(url, token)
}
