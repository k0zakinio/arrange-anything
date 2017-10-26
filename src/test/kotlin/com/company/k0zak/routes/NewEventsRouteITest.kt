package com.company.k0zak.routes

import com.company.k0zak.Server
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db_helpers.DBHelper
import com.company.k0zak.model.Event
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.lens.*
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class NewEventsRouteITest {

    private val okHttpClient = OkHttp()

    companion object {
        val server = Server(EventsDao(JDBCClient(DBHelper.testConfig)))
        @BeforeClass @JvmStatic fun beforeAll() {
            server.start()
            DBHelper.cleanDatabase()
        }

        @AfterClass @JvmStatic fun afterAll() {
            server.stop()
        }
    }

    @Test
    fun canPostNewEvent() {
        val response = postEvent(Event("testOwner", "testTitle"))

        assertEquals(Status.CREATED, response.status)
    }

    private fun postEvent(event: Event): Response {
        val titleField = FormField.string().required("title")
        val ownerField = FormField.string().required("owner")

        val lens = Body.webForm(Validator.Strict, titleField, ownerField).toLens()

        val webForm = WebForm().with(titleField of event.title).with(ownerField of event.owner)

        val request = Request(Method.POST, "http://localhost:8080/new").with(lens of webForm)

        return okHttpClient(request)
    }

}