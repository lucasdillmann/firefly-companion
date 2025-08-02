package br.com.dillmann.fireflycompanion.thirdparty.connectiontest

import br.com.dillmann.fireflycompanion.business.connectiontest.ConnectionTestRepository
import br.com.dillmann.fireflycompanion.thirdparty.firefly.apis.AboutApi
import br.com.dillmann.fireflycompanion.thirdparty.firefly.infrastructure.ApiClient
import okhttp3.Interceptor

internal class ConnectionTestHttpRepository : ConnectionTestRepository {
    override suspend fun isServerReachable(url: String, token: String): Boolean {
        val authorizer = Interceptor {
            val request = it.request().newBuilder().header("Authorization", "Bearer $token").build()
            it.proceed(request)
        }
        val client = ApiClient.defaultClient.newBuilder().addInterceptor(authorizer).build()
        val api = AboutApi(basePath = "$url/api", client)

        return try {
            api.getCurrentUser()
            true
        } catch (_: Exception) {
            false
        }
    }
}
