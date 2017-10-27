package com.company.k0zak.dao

import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.db_helpers.TestDBHelper.testDbClient
import com.company.k0zak.model.Event
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import org.junit.Before
import org.junit.Test

class EventsDaoTest {

    private val eventsDao = EventsDao(testDbClient)

    @Before
    fun beforeEach() {
        TestDBHelper.cleanDatabase()
    }

    @Test
    fun insertAndRetrieveAnEvent() {
        val event = Event("transform_owner", "transform_title")
        eventsDao.insertEvent(event)

        assertThat(eventsDao.getAllEvents(), hasElement(event))
    }
}