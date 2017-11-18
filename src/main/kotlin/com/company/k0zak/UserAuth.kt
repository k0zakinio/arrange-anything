package com.company.k0zak

import com.company.k0zak.dao.UserDao
import org.http4k.core.*
import org.http4k.core.cookie.cookies
import org.http4k.filter.ServerFilters.ReplaceResponseContentsWithStaticFile
import org.http4k.routing.ResourceLoader

class UserAuth(private val passwordHasher: Hasher, private val userDao: UserDao) {


    fun authenticated(storedPassword: String, currentPassword: String): Boolean {
        return passwordHasher.check(currentPassword, storedPassword)
    }

    fun hash(plainText: String): String {
        return passwordHasher.hashString(plainText)
    }

    private val failingStatusCodeToStaticHtml = ReplaceResponseContentsWithStaticFile(
            ResourceLoader.Classpath("/public/"),
            { if (it.status.successful) null else "${it.status.code}.html" })

    val authUserFilter: Filter = failingStatusCodeToStaticHtml.then(Filter { next: HttpHandler ->
        { req: Request ->
            val cookie = req.cookies().firstOrNull { it.name == "aa_session_id" }
            if (cookie == null || (userDao.getUserFromCookie(cookie.value) == null)) {
                Response(Status.UNAUTHORIZED)
            } else next(req)
        }
    })
}
