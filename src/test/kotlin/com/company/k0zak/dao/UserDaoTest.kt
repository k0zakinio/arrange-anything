package com.company.k0zak.dao

import com.company.k0zak.UserAuthenticator
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

    private val userDao = UserDao(testDbClient, UserAuthenticator())

    @Test
    fun canInsertAUserIntoTheDatabase() {
        val user = User("Mr Andrew", "secret")
        userDao.newUser(user)

        val authenticatedUser = AuthenticatedUser("Mr Andrew")

        assertThat(userDao.getUser(user), equalTo(authenticatedUser))
    }
}