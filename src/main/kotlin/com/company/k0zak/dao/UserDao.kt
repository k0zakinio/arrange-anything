package com.company.k0zak.dao

import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User

interface UserDao {
    fun newUser(user: User)
    fun getUser(username: String): User?
    fun newSession(user: User, sessionId: String)
    fun getUserFromCookie(cookie: String): AuthenticatedUser?
}