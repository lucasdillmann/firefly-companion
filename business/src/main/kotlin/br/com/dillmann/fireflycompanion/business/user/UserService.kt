package br.com.dillmann.fireflycompanion.business.user

internal class UserService(private val repository: UserRepository) {
    fun getCurrent() =
        repository.getCurrent()
}
