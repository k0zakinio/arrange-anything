package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.db.JDBCConfig
import com.company.k0zak.db.JDBCClient

object Dependencies {
    private val pgConfig = JDBCConfig(
            username = "postgres",
            password = "testpassword",
            uri = "postgresql://postgres.local:5432/testdb",
            driver = "org.postgresql.Driver"
    )
    private val pgClient = JDBCClient(pgConfig)
    val eventsDao = EventsDao(pgClient)
}

