package dev.jette.myphone.demo

import org.koin.core.annotation.Factory

@Factory
class UserPresenter(private val repository: UserRepository) {

    fun sayHello(name: String): String {
        val foundUser = repository.findUser(name)
        return foundUser?.let { "Hello '$it' from $this" } ?: "User '$name' not found!"
    }
}
