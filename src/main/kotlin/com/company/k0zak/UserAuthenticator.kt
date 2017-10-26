package com.company.k0zak

import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User

class UserAuthenticator {

    fun authenticate(storedUser: User, loginUser: User): AuthenticatedUser? {
        return if(PasswordHasher.check(loginUser.password, storedUser.password)) {
            AuthenticatedUser(loginUser.username)
        } else null
    }

    fun hashPassword(plainText: String): String = PasswordHasher.getSaltedHash(plainText)


}
