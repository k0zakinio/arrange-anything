package com.company.k0zak.routes

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.model.Event
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel

class ViewEventsRoute(private val eventsDao: EventsDao) {

    private val renderer = HandlebarsTemplates().CachingClasspath("view")

    val byId: HttpHandler = {
        val id: String = it.path("id")!!
        val event: Event? = eventsDao.getEventForId(id)

        if (event == null) {
            Response(NOT_FOUND).body("Event with id: $id was not found!")
        } else {
            val viewModel = Event(event.owner, event.title)
            val renderedView = renderer(viewModel)
            Response(OK).body(renderedView)
        }
    }

    data class EventsViewModel(val events: List<Event>): ViewModel {
        override fun template(): String {
            return "all_events"
        }
    }
    val all: HttpHandler = {
        val allEvents = EventsViewModel(eventsDao.getAllEvents())
        val renderedView = renderer(allEvents)
        Response(OK).body(renderedView)
    }
}