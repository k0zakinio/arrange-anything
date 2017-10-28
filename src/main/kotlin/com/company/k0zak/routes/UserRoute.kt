package com.company.k0zak.routes

import com.company.k0zak.UserAuthenticator
import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User
import org.http4k.core.*
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookie
import org.http4k.lens.*
import org.http4k.template.HandlebarsTemplates
import java.util.*

class UserRoute(userDao: UserDao, userAuthenticator: UserAuthenticator) {

    private val renderer = HandlebarsTemplates().CachingClasspath("view")

    private val usernameField = FormField.string().required("username")
    private val passwordField = FormField.string().required("password")
    private val formBody = Body.webForm(Validator.Strict, usernameField, passwordField).toLens()

    val newUser: HttpHandler = { req: Request ->
        try {
            val webForm = formBody.extract(req)
            val username = usernameField.extract(webForm)
            val password = passwordField.extract(webForm)

            val user = User(username, userAuthenticator.hash(password))
            userDao.newUser(user)

            Response(Status.SEE_OTHER).header("Location", "/created")

        } catch (e: LensFailure) {
            println(e.message)
            Response(Status.BAD_REQUEST).body("Unable to create event because fields were missing!")
        }
    }

    val loginUser: HttpHandler = { req: Request ->
        try {
            val webForm = formBody.extract(req)
            val username = usernameField.extract(webForm)

            val user = userDao.getUser(username)
            if(user != null) {
                val cookie = Cookie("aa_session_id", userAuthenticator.hash(UUID.randomUUID().toString()))
                val rendered = renderer(AuthenticatedUser(username))
                Response(Status.OK).body(rendered).cookie(cookie)
            } else Response(Status.FORBIDDEN).body("Invalid username or password!")
        } catch (e:LensFailure) {
            e.printStackTrace()
            Response(Status.INTERNAL_SERVER_ERROR).body("An unexpected error has occurred.")
        }
    }
}