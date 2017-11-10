package com.company.k0zak.routes

import com.company.k0zak.UserAuth
import com.company.k0zak.dao.PostgresUserDao
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.lens.*
import org.http4k.template.HandlebarsTemplates
import java.util.*

class UserRoute(userDao: PostgresUserDao, userAuth: UserAuth) {

    private val renderer = HandlebarsTemplates().CachingClasspath("view")

    private val usernameField = FormField.string().required("username")
    private val passwordField = FormField.string().required("password")
    private val formBody = Body.webForm(Validator.Strict, usernameField, passwordField).toLens()

    val newUser: HttpHandler = { req: Request ->
        println("A request has been received to create a new user")
        println("host: ${req.headers}")
        try {
            println("val webForm = formBody.extract(req)")
            val webForm = formBody.extract(req)

            println("val username = usernameField.extract(webForm)")
            val username = usernameField.extract(webForm)

            println("val password = passwordField.extract(webForm)")
            val password = passwordField.extract(webForm)

            println("val user = User(username, userAuth.hash(password))")
            val user = User(username, userAuth.hash(password))

            userDao.newUser(user)
            userDao.newUser(user)

            println("Received request for new user with u:$username and p:$password")

            Response(Status.MOVED_PERMANENTLY).header("Location", "https://www.google.com")

        } catch (e: LensFailure) {
            println(e.message)
            Response(Status.BAD_REQUEST).body("Unable to create user because fields were missing!")
        }
    }

    val loginUser: HttpHandler = { req: Request ->
        try {
            val webForm = formBody.extract(req)
            val username = usernameField.extract(webForm)
            val password = passwordField.extract(webForm)
            val user = userDao.getUser(username)

            if(user != null && userAuth.authenticated(user.password, password)) {
                val sessionId = userAuth.hash(UUID.randomUUID().toString())
                userDao.newSession(user, sessionId)
                val cookie = Cookie("aa_session_id", sessionId)
                val rendered = renderer(AuthenticatedUser(username))
                Response(Status.OK).body(rendered).cookie(cookie)
            } else Response(Status.FORBIDDEN).body("Invalid username or password!")
        } catch (e:LensFailure) {
            e.printStackTrace()
            Response(Status.INTERNAL_SERVER_ERROR).body("An unexpected error has occurred.")
        }
    }
}