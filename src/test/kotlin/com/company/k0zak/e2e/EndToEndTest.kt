package com.company.k0zak.e2e

import com.company.k0zak.PasswordHasher
import com.company.k0zak.Server
import com.company.k0zak.UserAuthenticator
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
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
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.Cookie as SeleniumCookie

class EndToEndTest {

    private val driver = TestDrivers.getChromeDriver()

    companion object {
        private val eventsDao = EventsDao(testDbClient)
        private val userDao = UserDao(testDbClient)
        private val server = Server(eventsDao, userDao, UserAuthenticator(PasswordHasher()))
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
    fun canInsertAnEventAndThenViewIt() {
        driver.get("http://localhost:8080/new")

        getElementByIdAndExecute("owner", { it.sendKeys("I am an Owner") })
        getElementByIdAndExecute("title", {
            it.sendKeys("I am a Title")
            it.submit()
        })

        driver.get("http://localhost:8080/view/1")

        val event = driver.findElementByTagName("h2")
        assertThat(event.text, equalTo("Owner: I am a Title | Title: I am an Owner"))
    }

    @Test
    fun canCreateANewAccountAndLogin() {
        driver.get("http://localhost:8080/create-account")

        getElementByIdAndExecute("username", { it.sendKeys("andrew") })
        getElementByIdAndExecute("password", {
            it.sendKeys("password")
            it.submit()
        })

        WebDriverWait(driver, 1).until(ExpectedConditions.textToBePresentInElementLocated(By.id("banner"), "Account created"))

        driver.findElementById("login").click()

        getElementByIdAndExecute("username", { it.sendKeys("andrew") })
        getElementByIdAndExecute("password", {
            it.sendKeys("password")
            it.submit()
        })

        WebDriverWait(driver, 3).until(ExpectedConditions.textToBePresentInElementLocated(By.id("banner"), "Welcome andrew"))
        val cookie = driver.manage().cookies.first().toHttp4kCookie()
        assertThat(cookie.name, equalTo("aa_session_id"))
        assertThat(cookie.path, equalTo("/"))
        assertThat(cookie.value, !isNullOrBlank)
    }

    private fun getElementByIdAndExecute(id: String, fn: (WebElement) -> Any): WebElement {
        val findElementById: WebElement = driver.findElementById(id)
        fn(findElementById)
        return findElementById
    }

    private fun SeleniumCookie.toHttp4kCookie(): Cookie = Cookie(name = this.name, value = this.value.replace("\"", ""), path = this.path)
}

