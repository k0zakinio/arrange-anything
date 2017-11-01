package com.company.k0zak

import com.company.k0zak.dao.UserDao
import org.http4k.core.*
import org.http4k.core.cookie.cookies

class UserAuthenticator(private val passwordHasher: Hasher, private val userDao: UserDao) {

    fun authenticated(storedPassword: String, currentPassword: String): Boolean {
        return passwordHasher.check(currentPassword, storedPassword)
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
