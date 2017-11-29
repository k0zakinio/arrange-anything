package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.PostgresUserDao
import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db.JDBCConfig
import java.time.format.DateTimeFormatter

object Dependencies {
    private val pgConfig = JDBCConfig(
            username = "postgres",
            password = "testpassword",
            uri = "postgres.local:5432/testdb",
            driver = "org.postgresql.Driver"
    )
    private val pgClient = JDBCClient(pgConfig)
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val eventsDao = EventsDao(pgClient, EventDateParser(dateTimeFormatter), EventDatePrinter(dateTimeFormatter))
    val userDao: PostgresUserDao = PostgresUserDao(pgClient)
    val userAuthenticator = UserAuth(PasswordHasher(), userDao)
}

