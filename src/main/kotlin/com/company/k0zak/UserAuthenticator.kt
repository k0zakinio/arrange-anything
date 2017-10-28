package com.company.k0zak

import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User

class UserAuthenticator(private val passwordHasher: Hasher) {

    fun authenticate(storedUser: User, loginUser: User): AuthenticatedUser? {
        return if(passwordHasher.check(loginUser.password, storedUser.password)) {
            AuthenticatedUser(loginUser.username)
        } else null
    }

    fun hash(plainText: String): String = passwordHasher.hashString(plainText)
}
