package com.company.k0zak.dao

import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.db_helpers.TestDBHelper.testDbClient
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Before
import org.junit.Test

class UserDaoTest {

    @Before
    fun beforeEach() {
        TestDBHelper.cleanDatabase()
    }

    private val userDao = UserDao(testDbClient)

    @Test
    fun canInsertAUserIntoTheDatabase() {
        val user = User("Mr Andrew", "stubbedHashedString")
        userDao.newUser(user)

        assertThat(userDao.getUser("Mr Andrew"), equalTo(user))
    }

    @Test
    fun `inserts a session_id for a user when they log in`() {
        val user = User("Itsa me", "Mario")
        userDao.newUser(user)
        userDao.newSession(user, "stubbedHashedString")

        val sessionId: String = TestDBHelper.executeQuery("SELECT session_id FROM SESSION", { it.getString(1) })

        assertThat(sessionId, equalTo("stubbedHashedString"))
    }

    @Test
    fun `retrieves an authenticatedUser if a valid sessionId is provided`() {
        val user = User("CookieMonster", "foobyBar")
        userDao.newUser(user)
        userDao.newSession(user, "stubbedHashedString")

        val userFromCookie = userDao.getUserFromCookie("stubbedHashedString")

        assertThat(userFromCookie, equalTo(AuthenticatedUser("CookieMonster")))
    }
}

