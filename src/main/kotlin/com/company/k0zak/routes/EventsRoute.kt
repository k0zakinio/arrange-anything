package com.company.k0zak.routes

import com.company.k0zak.UserAuthenticator
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.Event
import org.http4k.core.*
import org.http4k.core.cookie.cookie
import org.http4k.format.Jackson.auto
import org.http4k.lens.*
import org.http4k.routing.path
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel

class EventsRoute(private val eventsDao: EventsDao, private val userDao: UserDao, authentication: UserAuthenticator) {

    data class JsonResponse(val message: String)
    private val responseLens = Body.auto<JsonResponse>().toLens()
    private val renderer = HandlebarsTemplates().CachingClasspath("view")

    val postNew: HttpHandler = authentication.authenticateFilter.then({ req: Request ->
        val titleField = FormField.string().required("title")

        val formBody = Body.webForm(Validator.Strict, titleField).toLens()

        try {
            val webForm = formBody.extract(req)
            val title = titleField.extract(webForm)
            userDao.getUserFromCookie(req.cookie("aa_session_id")!!.value)
                    ?.let {
                        user -> eventsDao.insertEvent(Event(user.username, title))
                    }

            responseLens.inject(
                    JsonResponse("A postNew event has been created with the title $title"), Response(Status.CREATED))

        } catch (e: LensFailure) {
            println(e.message)
            responseLens.inject(
                    JsonResponse("Unable to create event because fields were missing!"), Response(Status.BAD_REQUEST))
        }
    })

    data class ViewUserEventsModel(val username: String, val events: List<Event>, val pathUser: String): ViewModel {
        override fun template(): String {
            return "user_events"
        }
    }

    val forUser: HttpHandler = authentication.authenticateFilter.then({ req: Request ->
        val pathUser = req.path("id")!!
        val user = userDao.getUserFromCookie(req.cookie("aa_session_id")!!.value)!!
        val allEvents = eventsDao.getEventsForUser(user.username)
        val rendered = renderer(ViewUserEventsModel(user.username, allEvents, pathUser))
        Response(Status.OK).body(rendered)
    })
}