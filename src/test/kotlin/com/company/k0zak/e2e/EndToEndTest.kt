package com.company.k0zak.e2e

import com.company.k0zak.PasswordHasher
import com.company.k0zak.Server
import com.company.k0zak.UserAuthenticator
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.PostgresUserDao
import com.company.k0zak.db_helpers.TestDBHelper
import com.company.k0zak.db_helpers.TestDBHelper.testDbClient
import com.company.k0zak.web_helpers.TestDrivers
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isNullOrBlank
import org.http4k.client.OkHttp
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookies
import org.http4k.lens.*
import org.junit.*
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.Cookie as SeleniumCookie

class EndToEndTest {

    private var driver = TestDrivers.getChromeDriver()

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
        driver.quit()
    }

    @Before
    fun startDriver() {
        driver = TestDrivers.getChromeDriver()
    }

    @Test
    fun `can create an account, login and have a cookie set`() {
        createAccount("andrew", "password")
        login("andrew", "password")
        getElementByAndThen(By.id("banner"), { assertThat(it.text, equalTo("Welcome andrew")) })

        val cookie = driver.manage().cookies.first().toHttp4kCookie()
        assertThat(cookie.name, equalTo("aa_session_id"))
        assertThat(cookie.path, equalTo("/"))
        assertThat(cookie.value, !isNullOrBlank)
    }

    @Test
    fun `user can view an event they have created`() {
        val cookie = postCreateAccountAndLogin("testy", "password")
        driver.get("http://localhost:8080/create-account")
        val seleniumCookie = org.openqa.selenium.Cookie(cookie.name, cookie.value)
        driver.manage().addCookie(seleniumCookie)
        driver.get("http://localhost:8080/new")



        getElementByAndThen(By.id("title"), { it.sendKeys("This is my test Event!"); it.submit() })

        driver.get("http://localhost:8080/users/testy")

        getElementByAndThen(By.className("event-title"), { assertThat(it.text, equalTo("This is my test Event!"))})
    }

    private fun postCreateAccountAndLogin(username: String, password: String): Cookie {
        val client = OkHttp()
        val passwordField = FormField.string().required("password")
        val usernameField = FormField.string().required("username")
        val strictForm = Body.webForm(Validator.Strict, usernameField, passwordField).toLens()
        val webForm = WebForm().with(usernameField of username, passwordField of password)

        client(Request(Method.POST, "http://localhost:8080/create-account")
                .with(strictForm of webForm)).close()

        val login = client(Request(Method.POST, "http://localhost:8080/login")
                .with(strictForm of webForm))
        return login.cookies().first()
    }

    private fun login(username: String, password: String) {
        getElementByAndThen(By.id("login"), { it.click() })

        getElementByAndThen(By.id("username"), { it.sendKeys(username) })
        getElementByAndThen(By.id("password"), {
            it.sendKeys(password)
            it.submit()
        })
    }

    private fun createAccount(username: String, password: String) {
        driver.get("http://localhost:8080/create-account")

        getElementByAndThen(By.id("username"), { it.sendKeys(username) })
        getElementByAndThen(By.id("password"), {
            it.sendKeys(password)
            it.submit()
        })
    }

    private fun getElementByAndThen(by: By, fn: (WebElement) -> Any): WebElement {
        val findElementById: WebElement = driver.findElement(by)
        fn(findElementById)
        return findElementById
    }

    private fun SeleniumCookie.toHttp4kCookie(): Cookie = Cookie(name = this.name, value = this.value.replace("\"", ""), path = this.path)

    private val OK = Matcher<Status>("equals Status.OK", { it == Status.OK })
    private val SEE_OTHER = Matcher<Status>("equals Status.CREATED", { it == Status.SEE_OTHER })
}

