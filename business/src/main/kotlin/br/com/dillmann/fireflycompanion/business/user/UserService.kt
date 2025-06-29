package br.com.dillmann.fireflycompanion.business.user

internal class UserService(private val repository: UserRepository) {
    suspend fun getCurrent() =
        repository.getCurrent()
}
