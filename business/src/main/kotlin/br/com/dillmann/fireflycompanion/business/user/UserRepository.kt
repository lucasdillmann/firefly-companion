package br.com.dillmann.fireflycompanion.business.user

interface UserRepository {
    suspend fun getCurrent(): User
}
