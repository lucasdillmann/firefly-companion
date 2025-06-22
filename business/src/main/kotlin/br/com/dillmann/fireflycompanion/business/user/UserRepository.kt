package br.com.dillmann.fireflycompanion.business.user

interface UserRepository {
    fun getCurrent(): User
}
