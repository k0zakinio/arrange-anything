package com.company.k0zak.routes

import com.company.k0zak.Server
import com.company.k0zak.db_helpers.DBHelper
import com.company.k0zak.model.Event
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class EventsRouteITest {

    private val okHttpClient = OkHttp()

    companion object {
        @BeforeClass @JvmStatic fun beforeAll() {
            Server.start()
            DBHelper.cleanDatabase()
        }
    }

    @Test
    fun canPostNewEvent() {
        val response = postEvent(Event("testOwner", "testTitle"))

        assertEquals(Status.CREATED, response.status)
    }

    @Test
    fun canGetEvents() {
        postEvent(Event("getTest", "getTestTitle"))
        val request = Request(Method.GET, "http://localhost:8080/events")

        val response = okHttpClient(request)

        assertEquals("[{\"owner\":\"getTest\",\"title\":\"getTestTitle\"}]", response.bodyString())
    }

    private fun postEvent(event: Event): Response {
        val body = Body.invoke("{\"owner\":\"${event.owner}\",\"title\":\"${event.title}\"}")

        val request = Request(Method.POST, "http://localhost:8080/events")
                .body(body)
                .header("Content-Type", "application/json")

        return okHttpClient(request)
    }

}