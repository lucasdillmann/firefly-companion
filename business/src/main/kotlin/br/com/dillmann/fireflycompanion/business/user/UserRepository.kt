package br.com.dillmann.fireflycompanion.business.user

fun interface UserRepository {
    suspend fun getCurrent(): User
}
