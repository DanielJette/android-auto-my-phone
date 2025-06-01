package dev.jette.myphone.demo

import org.koin.core.annotation.Single

interface UserRepository {
    fun findUser(name: String): User?
    fun addUsers(users: List<User>)
}

@Single
class UserRepositoryImpl : UserRepository {

    private val _users = arrayListOf<User>()

    override fun findUser(name: String): User? {
        return _users.firstOrNull { it.name == name }
    }

    override fun addUsers(users: List<User>) {
        _users.addAll(users)
    }
}