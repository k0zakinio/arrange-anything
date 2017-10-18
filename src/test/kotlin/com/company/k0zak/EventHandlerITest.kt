package com.company.k0zak

import org.flywaydb.core.Flyway
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class EventHandlerITest {

    private val okHttpClient = OkHttp()

    companion object {
        @BeforeClass @JvmStatic fun beforeAll() {
            App.start()
            cleanDatabase()
        }

        private fun cleanDatabase() {
            val flyway = Flyway()
            flyway.setDataSource("jdbc:postgresql://postgres.local:5432/testdb", "postgres", "testpassword")
            flyway.clean()
            flyway.migrate()
        }
    }

    @Test
    fun canInsertQueriesIntoTheDatabase() {
        val response = insertEvent("testOwner", "testTitle")

        assertEquals(Status.CREATED, response.status)
    }

    @Test
    fun canRetrieveEventsFromTheDatabase() {
        insertEvent("getTest", "getTestTitle")
        val request = Request(Method.GET, "http://localhost:8080/event?owner=getTest")

        val response = okHttpClient(request)

        assertEquals("[{\"title\" : \"getTestTitle\", \"owner\" : \"getTest\"}]", response.bodyString())
    }

    private fun insertEvent(owner: String, title: String): Response {
        val body = MultipartFormBody()
                .plus("owner" to owner)
                .plus("title" to title)

        val request = Request(Method.POST, "http://localhost:8080/event")
                .body(body)
                .header("content-type", "multipart/form-data; boundary=${body.boundary}")

        return okHttpClient(request)
    }

}