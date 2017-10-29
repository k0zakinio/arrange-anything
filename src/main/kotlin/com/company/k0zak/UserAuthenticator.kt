package com.company.k0zak

import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User
import org.http4k.core.*
import org.http4k.core.cookie.cookies

class UserAuthenticator(private val passwordHasher: Hasher, private val userDao: UserDao) {

    fun authenticate(storedUser: User, loginUser: User): AuthenticatedUser? {
        return if(passwordHasher.check(loginUser.password, storedUser.password)) {
            AuthenticatedUser(loginUser.username)
        } else null
    }

    fun hash(plainText: String): String = passwordHasher.hashString(plainText)

    val authenticateFilter: Filter = Filter { next: HttpHandler ->
        { req: Request ->
            val cookie = req.cookies().firstOrNull { it.name == "aa_session_id" }
            if(cookie == null || (userDao.getUserFromCookie(cookie.value) == null)) {
                val unauthorizedHtml = this.javaClass.getResourceAsStream("/public/unauthorized.html").bufferedReader().use { it.readLine() }
                Response(Status.UNAUTHORIZED).body(unauthorizedHtml).header("Content-Type", "text/html")
            }
            else next(req)
        }
    }
}
