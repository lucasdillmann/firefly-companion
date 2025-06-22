package br.com.dillmann.fireflycompanion.business.serverconfig

import br.com.dillmann.fireflycompanion.core.validation.ValidationOutcome
import java.net.URI
import java.util.*

internal class ServerConfigValidator {
    fun validate(serverConfig: ServerConfig) {
        val outcome = ValidationOutcome()

        validateUrlNotBlank(serverConfig.url, outcome)
        validateUrlIsValidHttpOrHttps(serverConfig.url, outcome)

        validateTokenNotBlank(serverConfig.token, outcome)
        validateTokenIsValidJwt(serverConfig.token, outcome)

        outcome.throwExceptionIfNeeded()
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
        if (parts.size != 3)
            return false

        return try {
            parts.forEach { Base64.getDecoder().decode(it.padBase64()) }
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun String.padBase64(): String {
        val padding = when (length % 4) {
            0 -> 0
            1 -> 3
            2 -> 2
            else -> 1
        }
        return this + "=".repeat(padding)
    }
}
