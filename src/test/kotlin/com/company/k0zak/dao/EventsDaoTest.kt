package com.company.k0zak.dao

import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db_helpers.DBHelper
import com.company.k0zak.model.Event
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EventsDaoTest {

    private val eventsDao = EventsDao(JDBCClient(DBHelper.testConfig))

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