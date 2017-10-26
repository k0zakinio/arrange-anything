package com.company.k0zak.routes

import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.User
import org.http4k.core.*
import org.http4k.lens.*
import org.http4k.template.HandlebarsTemplates

class UserRoute(userDao: UserDao) {
    data class JsonResponse(val message: String)

    private val renderer = HandlebarsTemplates().CachingClasspath("view")

    private val usernameField = FormField.string().required("username")
    private val passwordField = FormField.string().required("password")
    private val formBody = Body.webForm(Validator.Strict, usernameField, passwordField).toLens()

    val newUser: HttpHandler = { req: Request ->
        try {
            val webForm = formBody.extract(req)
            val username = usernameField.extract(webForm)
            val password = passwordField.extract(webForm)

            val user = User(username, password)
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
            val password = passwordField.extract(webForm)

            val user = User(username, password)

            val authenticatedUser = userDao.getUser(user)
            if(authenticatedUser != null) {
                val rendered = renderer(authenticatedUser)
                Response(Status.OK).body(rendered)
            } else Response(Status.FORBIDDEN).body("Invalid username or password!")
        } catch (e:LensFailure) {
            e.printStackTrace()
            Response(Status.INTERNAL_SERVER_ERROR).body("An unexpected error has occurred.")
        }
    }
}