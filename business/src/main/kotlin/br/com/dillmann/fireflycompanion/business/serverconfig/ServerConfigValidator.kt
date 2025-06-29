package br.com.dillmann.fireflycompanion.business.serverconfig

import br.com.dillmann.fireflycompanion.business.connectiontest.ConnectionTestService
import br.com.dillmann.fireflycompanion.core.validation.MessageException
import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome
import java.net.URI

internal class ServerConfigValidator(private val connectionTestService: ConnectionTestService) {
    suspend fun validate(serverConfig: ServerConfig) {
        val outcome = ValidationOutcome()

        validateUrlNotBlank(serverConfig.url, outcome)
        validateUrlIsValidHttpOrHttps(serverConfig.url, outcome)

        validateTokenNotBlank(serverConfig.token, outcome)
        validateTokenIsValidJwt(serverConfig.token, outcome)

        outcome.throwExceptionIfNeeded()

        validateServerReachable(serverConfig)
    }

    private fun validateUrlNotBlank(url: String, outcome: ValidationOutcome) {
        if (url.isBlank())
            outcome.addViolation("url", "URL should not be blank, empty or null")
    }

    private fun validateUrlIsValidHttpOrHttps(url: String, outcome: ValidationOutcome) {
        if (url.isBlank()) return

        try {
            val urlObj = URI(url)
            val protocol = urlObj.scheme.lowercase()
            if (protocol != "http" && protocol != "https")
                outcome.addViolation("url", "URL should be a valid HTTP or HTTPS URL")
        } catch (_: Exception) {
            outcome.addViolation("url", "URL should be a valid HTTP or HTTPS URL")
        }
    }

    private fun validateTokenNotBlank(token: String, outcome: ValidationOutcome) {
        if (token.isBlank())
            outcome.addViolation("token", "Token should not be blank, empty or null")
    }

    private fun validateTokenIsValidJwt(token: String, outcome: ValidationOutcome) {
        if (token.isBlank())
            return

        if (!isValidJwt(token))
            outcome.addViolation("token", "Token should be a valid JWT")
    }

    private fun isValidJwt(token: String): Boolean {
        val parts = token.split(".")
        return parts.size == 3 && parts.all { it.isNotEmpty() }
    }

    private suspend fun validateServerReachable(config: ServerConfig) {
        val reachable = connectionTestService.isServerReachable(config.url, config.token)
        if (!reachable)
            throw MessageException(
                "Server is not reachable",
                "We're unable to connect to the server with the given URL and token. Please check that everything is correct.",
            )
    }
}
