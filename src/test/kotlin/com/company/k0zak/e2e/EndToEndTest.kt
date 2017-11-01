package com.company.k0zak.e2e

import com.company.k0zak.PasswordHasher
import com.company.k0zak.Server
import com.company.k0zak.UserAuthenticator
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.PostgresUserDao
import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.db_helpers.TestDBHelper.testDbClient
import com.company.k0zak.web_helpers.TestDrivers
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isNullOrBlank
import org.http4k.core.cookie.Cookie
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.WebElement
import org.openqa.selenium.Cookie as SeleniumCookie

class EndToEndTest {

    private val driver = TestDrivers.getChromeDriver()

    companion object {
        private val eventsDao = EventsDao(testDbClient)
        private val userDao = PostgresUserDao(testDbClient)
        private val server = Server(eventsDao, userDao, UserAuthenticator(PasswordHasher(), userDao))
        @BeforeClass @JvmStatic fun beforeAll() {
            server.start()
            TestDBHelper.cleanDatabase()
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
    fun `you must be logged in to create an event`() {
        driver.get("http://localhost:8080/new")

        val banner = driver.findElementByTagName("h2")

        assertThat(banner.text, equalTo("You are not authorized to access this resource."))
    }

    @Test
    fun `can create an account, login and have a cookie set`() {
        createAccount("andrew", "password")
        login("andrew", "password")
        getElementByIdAndThen("banner", { assertThat(it.text, equalTo("Welcome andrew")) })

        val cookie = driver.manage().cookies.first().toHttp4kCookie()
        assertThat(cookie.name, equalTo("aa_session_id"))
        assertThat(cookie.path, equalTo("/"))
        assertThat(cookie.value, !isNullOrBlank)
    }

    private fun login(username: String, password: String) {
        getElementByIdAndThen("login", { it.click() })

        getElementByIdAndThen("username", { it.sendKeys(username) })
        getElementByIdAndThen("password", {
            it.sendKeys(password)
            it.submit()
        })
    }

    private fun createAccount(username: String, password: String) {
        driver.get("http://localhost:8080/create-account")

        getElementByIdAndThen("username", { it.sendKeys(username) })
        getElementByIdAndThen("password", {
            it.sendKeys(password)
            it.submit()
        })
    }

    private fun getElementByIdAndThen(id: String, fn: (WebElement) -> Any): WebElement {
        val findElementById: WebElement = driver.findElementById(id)
        fn(findElementById)
        return findElementById
    }

    private fun SeleniumCookie.toHttp4kCookie(): Cookie = Cookie(name = this.name, value = this.value.replace("\"", ""), path = this.path)
}

