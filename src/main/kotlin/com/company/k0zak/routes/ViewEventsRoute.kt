package com.company.k0zak.routes

import com.company.k0zak.EventDatePrinter
import com.company.k0zak.UserAuth
import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
import com.company.k0zak.model.Event
import com.company.k0zak.model.EventViewModel
import org.http4k.core.*
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.cookie.cookie
import org.http4k.routing.path
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel

class ViewEventsRoute(eventsDao: EventsDao, userDao: UserDao, auth: UserAuth, eventDatePrinter: EventDatePrinter) {

    private val renderer = HandlebarsTemplates().CachingClasspath("view")

    val byId: HttpHandler = auth.authUserFilter.then({
        val id: String = it.path("id")!!
        val event: Event? = eventsDao.getEventForId(id)

        if (event == null) {
            Response(NOT_FOUND).body("Event with id: $id was not found!")
        } else {
            val viewModel = EventViewModel(event.owner, event.title, eventDatePrinter.print(event.date))
            val renderedView = renderer(viewModel)
            Response(OK).body(renderedView)
        }
    })

    val forUser: HttpHandler = auth.authUserFilter.then({ req: Request ->
        val pathUser = req.path("id")!!
        val user = userDao.getUserFromCookie(req.cookie("aa_session_id")!!.value)!!
        val allEvents = eventsDao.getEventsForUser(user.username).map{ EventViewModel(it.owner, it.title, eventDatePrinter.print(it.date))}
        val rendered = renderer(ViewUserEventsModel(user.username, allEvents, pathUser))
        Response(Status.OK).body(rendered)
    })

    private data class ViewUserEventsModel(val username: String, val events: List<EventViewModel>, val pathUser: String): ViewModel {
        override fun template(): String {
            return "user_events"
        }
    }
}