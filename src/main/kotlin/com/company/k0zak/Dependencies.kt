package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db.JDBCConfig

object Dependencies {
    private val pgConfig = JDBCConfig(
            username = "postgres",
            password = "testpassword",
            uri = "postgres.local:5432/testdb",
            driver = "org.postgresql.Driver"
    )
    private val pgClient = JDBCClient(pgConfig)
    val eventsDao = EventsDao(pgClient)
    val userDao: UserDao = UserDao(pgClient)
    val userAuthenticator = UserAuthenticator(PasswordHasher(), userDao)
}

