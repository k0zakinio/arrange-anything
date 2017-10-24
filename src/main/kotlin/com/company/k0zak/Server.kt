package com.company.k0zak

import com.company.k0zak.routes.NewEventsRoute
import com.company.k0zak.routes.ViewEventsRoute
import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty

object Server {
    fun start() {

        val newEvents = NewEventsRoute(Dependencies.eventsDao)
        val viewEvents = ViewEventsRoute(Dependencies.eventsDao)

        val app: RoutingHttpHandler = routes(
                "/events" bind Method.POST to newEvents.post,
                "/view/{id}" bind Method.GET to viewEvents.byId,
                "/view" bind Method.GET to viewEvents.all
        )

        val port = 8080
        println("Server started on port: $port")
        Jetty(port).toServer(app).start()
    }
}

fun main(args: Array<String>) {
    Server.start()
}