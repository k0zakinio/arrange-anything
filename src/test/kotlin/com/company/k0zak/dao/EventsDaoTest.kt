package com.company.k0zak.dao

import com.company.k0zak.db.PgConfig
import com.company.k0zak.db.PostgresClient
import com.company.k0zak.db_helpers.DBHelper
import com.company.k0zak.model.Event
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EventsDaoTest {

    private val eventsDao = EventsDao(PostgresClient(PgConfig(
            username = "postgres",
            password = "testpassword",
            hostname = "postgres.local",
            port = 5432,
            dbName = "testdb"
    )))

    @Before
    fun beforeEach() {
        DBHelper.cleanDatabase()
    }

    @Test
    fun insertAndRetrieveAnEvent() {
        val event = Event("transform_owner", "transform_title")
        eventsDao.insertEvent(event)

        assertEquals(eventsDao.getAllEvents().first(), event)
    }
}