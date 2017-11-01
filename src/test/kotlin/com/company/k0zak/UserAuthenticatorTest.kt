package com.company.k0zak

import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.junit.Test

class UserAuthenticatorTest {


    @Test
    fun `should return an unauthorized status code when no cookie is provided`() {
        val userAuthenticator = UserAuthenticator(FakeHasher("stubbedPasswordHash"), FakeUserDao(null))
        val underTest: Filter = userAuthenticator.authenticateFilter
        val handler: HttpHandler = underTest.then { Response(Status.OK) }

        val resp: Response = handler(Request(Method.GET, "/foobar"))

        assertThat(resp.status, equalTo(Status.UNAUTHORIZED))
    }

    @Test
    fun `should not allow access if the filter is provided with an invalid cookie`() {
        val userAuthenticator = UserAuthenticator(FakeHasher("stubbedPasswordHash"), FakeUserDao(null))
        val underTest: Filter = userAuthenticator.authenticateFilter
        val handler: HttpHandler = underTest.then { Response(Status.OK) }

        val resp: Response = handler(Request(Method.GET, "/foobar").cookie(Cookie("aa_session_id", "someInvalidCookie")))

        assertThat(resp.status, equalTo(Status.UNAUTHORIZED))
    }

    @Test
    fun `should allow access if the filter is provided with a valid cookie`() {
        val userAuthenticator = UserAuthenticator(FakeHasher("stubbedPasswordHash"), FakeUserDao(User("AnAuthenticatedUser", "foobar")))
        val underTest: Filter = userAuthenticator.authenticateFilter
        val handler: HttpHandler = underTest.then { Response(Status.OK) }

        val resp: Response = handler(Request(Method.GET, "/foobar").cookie(Cookie("aa_session_id", "someValidCookie")))

        assertThat(resp.status, equalTo(Status.OK))
    }
}

class FakeUserDao(private val stubbedUser: User?): UserDao {
    override fun getUser(username: String): User? = stubbedUser

    override fun newSession(user: User, sessionId: String) = Unit

    override fun newUser(user: User) = Unit

    override fun getUserFromCookie(cookie: String): AuthenticatedUser? = stubbedUser?.let { user -> AuthenticatedUser(user.username) }
}