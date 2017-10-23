package com.company.k0zak

import com.company.k0zak.routes.EventsRoute
import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty

object Server {
    fun start() {

        val eventRoute = EventsRoute(Dependencies.eventsDao)

        val app: RoutingHttpHandler = routes(
                "/events" bind Method.GET to eventRoute.get,
                "/events" bind Method.POST to eventRoute.post
        )

        val port = 8080
        println("Server started on port: $port")
        Jetty(port).toServer(app).start()
    }
}

fun main(args: Array<String>) {
    Server.start()
}