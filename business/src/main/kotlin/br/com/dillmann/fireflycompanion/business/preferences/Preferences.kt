package br.com.dillmann.fireflycompanion.business.preferences

data class Preferences(
    val requireBiometricLogin: Boolean = false,
    val theme: Theme = Theme.AUTO,
) {
    enum class Theme(val description: String) {
        AUTO("Auto (follow system)"),
        LIGHT("Light mode"),
        DARK("Dark mode"),
    }
}
