package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.model.Event
import com.company.k0zak.model.User
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.junit.After
import org.junit.Before
import org.junit.Test

val fakeHashedValue = "stubbedHashValue"

class UserAuthenticatorTest {

    private val userDao = UserDao(TestDBHelper.testDbClient)
    private val eventsDao = EventsDao(TestDBHelper.testDbClient)
    private val server = Server(eventsDao, userDao, UserAuthenticator(FakeHasher(fakeHashedValue), userDao))

    @Before
    fun before() {
        TestDBHelper.cleanDatabase()
        server.start()
    }

    @After
    fun after() {
        server.stop()
    }

    @Test
    fun `should use a cookie if session cookie is available and valid`() {
        val user = User("Andrew", "doesntMatter")
        userDao.newUser(user)
        eventsDao.insertEvent(Event("Andrew", "This is my postNew event!"))
        userDao.newSession(user, fakeHashedValue)

        val okHttp = OkHttp()
        val response: Response = okHttp(Request(Method.GET, "http://localhost:8080/view/1").cookie(Cookie("aa_session_id", fakeHashedValue)))

        assertThat(response.status, equalTo(Status.OK))
    }

    @Test
    fun `should not allow access if the session cookie does not exist`() {
        userDao.newUser(User("Andrew", "doesntMatter"))
        eventsDao.insertEvent(Event("Andrew", "This is my postNew event!"))

        val okHttp = OkHttp()
        val response: Response = okHttp(Request(Method.GET, "http://localhost:8080/view/1"))

        assertThat(response.status, equalTo(Status.UNAUTHORIZED))
    }
}