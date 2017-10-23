package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.db.PgConfig
import com.company.k0zak.db.PostgresClient

object Dependencies {
    private val pgConfig = PgConfig("postgres", "testpassword", "postgres.local", 5432, "testdb")
    private val pgClient = PostgresClient(pgConfig)
    val eventsDao = EventsDao(pgClient)
}

