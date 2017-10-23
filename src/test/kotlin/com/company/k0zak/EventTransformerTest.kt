package com.company.k0zak

import com.company.k0zak.db_helpers.DBHelper
import com.company.k0zak.model.Event
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EventTransformerTest {

    @Before
    fun beforeEach() {
        DBHelper.cleanDatabase()
    }

    @Test
    fun insertAndRetrieveAnEvent() {
        val event = Event("transform_owner", "transform_title")
        EventTransformer.insertEvent(event)

        assertEquals(EventTransformer.getAllEvents().first(), event)
    }
}