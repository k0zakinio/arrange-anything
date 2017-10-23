package com.company.k0zak

import com.company.k0zak.db_helpers.DBHelper
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class RoutesITest {

    private val okHttpClient = OkHttp()

    companion object {
        @BeforeClass @JvmStatic fun beforeAll() {
            App.start()
            DBHelper.cleanDatabase()
        }
    }

    @Test
    fun canPostNewEvent() {
        val response = postEvent("testOwner", "testTitle")

        assertEquals(Status.CREATED, response.status)
    }

    @Test
    fun canGetEvents() {
        postEvent("getTest", "getTestTitle")
        val request = Request(Method.GET, "http://localhost:8080/event?owner=getTest")

        val response = okHttpClient(request)

        assertEquals("[{\"title\" : \"getTestTitle\", \"owner\" : \"getTest\"}]", response.bodyString())
    }

    private fun postEvent(owner: String, title: String): Response {
        val body = MultipartFormBody()
                .plus("owner" to owner)
                .plus("title" to title)

        val request = Request(Method.POST, "http://localhost:8080/event")
                .body(body)
                .header("content-type", "multipart/form-data; boundary=${body.boundary}")

        return okHttpClient(request)
    }

}