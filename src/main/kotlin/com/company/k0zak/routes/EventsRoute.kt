package com.company.k0zak.routes

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.model.Event
import org.http4k.core.*
import org.http4k.format.Jackson.auto

class EventsRoute(private val eventsDao: EventsDao) {

    val get: HttpHandler = {
        val responseLens = Body.auto<List<Event>>().toLens()
        val allEvents = eventsDao.getAllEvents()
        responseLens.inject(allEvents, Response(Status.OK))
    }

    val post: HttpHandler = { req: Request ->
        val eventLens = Body.auto<Event>().toLens()
        val event = eventLens.extract(req)
        eventsDao.insertEvent(event)
        Response(Status.CREATED).body("Event: $event was inserted into the database")
    }
}