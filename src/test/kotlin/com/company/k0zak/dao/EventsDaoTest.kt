package com.company.k0zak.dao

import com.company.k0zak.LocalDateTimeParser
import com.company.k0zak.LocalDateTimePrinter
import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.db_helpers.TestDBHelper.testDbClient
import com.company.k0zak.model.Event
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventsDaoTest {

    private val eventsDao = EventsDao(testDbClient, LocalDateTimeParser(DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTimePrinter(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

    private val someDate = LocalDateTime.of(2017, 1, 1, 0, 0)
    private val username = "test_owner"
    private val eventTitle = "test_title"
    private val anEvent = Event(username, eventTitle, someDate)

    @Before
    fun beforeEach() {
        TestDBHelper.cleanDatabase()
    }

    @Test
    fun insertAndRetrieveAnEvent() {
        eventsDao.insertEvent(anEvent)

        assertThat(eventsDao.getAllEvents(), hasElement(anEvent))
    }

    @Test
    fun `can retrieve an event for a user`() {
        eventsDao.insertEvent(anEvent)

        assertThat(eventsDao.getEventsForUser(username), equalTo(listOf(anEvent)))
    }
}