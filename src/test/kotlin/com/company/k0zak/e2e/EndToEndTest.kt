package com.company.k0zak.e2e

import com.company.k0zak.Server
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db_helpers.DBHelper
import com.company.k0zak.web_helpers.TestDrivers
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class EndToEndTest {

    private val driver = TestDrivers.getChromeDriver()

    companion object {
        private val server = Server(EventsDao(JDBCClient(DBHelper.testConfig)))
        @BeforeClass @JvmStatic fun beforeAll() {
            server.start()
            DBHelper.cleanDatabase()
        }

        @AfterClass
        @JvmStatic fun afterAll() {
            server.stop()
        }
    }

    @After
    fun closeDriver() {
        driver.close()
    }

    @Test
    fun canInsertAnEventAndThenViewIt() {
        driver.get("http://localhost:8080/new")

        val ownerElem = driver.findElementById("owner")
        ownerElem.sendKeys("I am an Owner")

        val titleElem = driver.findElementById("title")
        titleElem.sendKeys("I am a Title")

        ownerElem.submit()

        driver.get("http://localhost:8080/view/1")

        val event = driver.findElementByTagName("h2")
        assertEquals(event.text, "Owner: I am a Title | Title: I am an Owner")
    }
}

