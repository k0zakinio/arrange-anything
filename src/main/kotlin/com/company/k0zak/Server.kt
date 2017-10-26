package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.routes.NewEventsRoute
import com.company.k0zak.routes.ViewEventsRoute
import org.http4k.core.Method
import org.http4k.routing.*
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty

class Server(private val eventsDao: EventsDao) {
    private lateinit var server: Http4kServer

    fun start() {
        val newEvents = NewEventsRoute(eventsDao)
        val viewEvents = ViewEventsRoute(eventsDao)

        val app: RoutingHttpHandler = routes(
                "/new" bind Method.POST to newEvents.newPost,
                "/view/{id}" bind Method.GET to viewEvents.byId,
                "/view" bind Method.GET to viewEvents.all,
                "/new" bind Method.GET to static(ResourceLoader.Classpath("public/new"))
        )

        val port = 8080
        println("Server started on port: $port")
        this.server = Jetty(port).toServer(app).start()
    }

    fun stop() {
        server.stop()
    }
}

fun main(args: Array<String>) {
    Server(Dependencies.eventsDao).start()
}