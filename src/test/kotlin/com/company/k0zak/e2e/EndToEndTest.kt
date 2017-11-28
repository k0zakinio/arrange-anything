package com.company.k0zak.e2e

import com.company.k0zak.*
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.PostgresUserDao
import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.db_helpers.TestDBHelper.testDbClient
import com.company.k0zak.web_helpers.TestDrivers
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isNullOrBlank
import org.http4k.core.cookie.Cookie
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.openqa.selenium.Cookie as SeleniumCookie

class EndToEndTest {

    companion object {
        private val eventsDao = EventsDao(testDbClient, LocalDateTimeParser(DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTimePrinter(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        private val userDao = PostgresUserDao(testDbClient)
        private val server = Server(eventsDao, userDao, UserAuth(PasswordHasher(), userDao))
        private var driver = TestDrivers.getChromeDriver()
        @BeforeClass @JvmStatic fun beforeAll() {
            server.start()
            TestDBHelper.cleanDatabase()
        }

        @AfterClass
        @JvmStatic fun afterAll() {
            server.stop()
            driver.quit()
        }
    }

    @Before
    fun before() {
        driver.manage().deleteAllCookies()
        TestDBHelper.cleanDatabase()
    }

    @Test
    fun `can create an account, login and have a cookie set`() {
        createAccount("andrew", "password")
        login("andrew", "password")

        val cookie = driver.manage().cookies.first().toHttp4kCookie()
        assertThat(cookie.name, equalTo("aa_session_id"))
        assertThat(cookie.path, equalTo("/"))
        assertThat(cookie.value, !isNullOrBlank)
    }

    @Test
    fun `user can view an event they have created`() {
        createAndLogin("testy", "password")
        driver.get("http://localhost:8080/new")
        driver.executeScript("document.getElementById('event_date').value = '2017-06-13T12:00'")

        getElementByAndThen(By.id("title")) {
            it.sendKeys("This is my test Event!")
            it.submit()
        }

        driver.get("http://localhost:8080/users/testy")

        getElementByAndThen(By.className("event-title"), { assertThat(it.text, equalTo("This is my test Event!")) })
    }

    private fun createAndLogin(username: String, password: String) {
        driver.get("http://localhost:8080/create-account")
        getElementByAndThen(By.id("username")) {it.sendKeys(username)}
        getElementByAndThen(By.id("password")) {it.sendKeys(password); it.submit()}
        driver.get("http://localhost:8080/login")
        getElementByAndThen(By.id("username")) {it.sendKeys(username)}
        getElementByAndThen(By.id("password")) {it.sendKeys(password); it.submit()}
    }

    private fun login(username: String, password: String) {
        getElementByAndThen(By.id("login"), { it.click() })

        getElementByAndThen(By.id("username")) { it.sendKeys(username) }
        getElementByAndThen(By.id("password")) {
            it.sendKeys(password)
            it.submit()
        }
    }

    private fun createAccount(username: String, password: String) {
        driver.get("http://localhost:8080/create-account")

        getElementByAndThen(By.id("username")) { it.sendKeys(username) }
        getElementByAndThen(By.id("password")) {
            it.sendKeys(password)
            it.submit()
        }
    }

    private fun getElementByAndThen(by: By, fn: (WebElement) -> Any): WebElement {
        val findElementById: WebElement = driver.findElement(by)
        fn(findElementById)
        return findElementById
    }

    private fun SeleniumCookie.toHttp4kCookie(): Cookie = Cookie(name = this.name, value = this.value.replace("\"", ""), path = this.path)
}

