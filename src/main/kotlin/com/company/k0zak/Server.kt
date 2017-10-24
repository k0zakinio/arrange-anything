package com.company.k0zak

import com.company.k0zak.routes.NewEventsRoute
import com.company.k0zak.routes.ViewEventsRoute
import org.http4k.core.Method
import org.http4k.routing.*
import org.http4k.server.Jetty

object Server {
    fun start() {

        val newEvents = NewEventsRoute(Dependencies.eventsDao)
        val viewEvents = ViewEventsRoute(Dependencies.eventsDao)

        val app: RoutingHttpHandler = routes(
//                "/new" bind Method.GET to newEvents.create,
                "/new" bind Method.POST to newEvents.newPost,
                "/view/{id}" bind Method.GET to viewEvents.byId,
                "/view" bind Method.GET to viewEvents.all,
                "/new" bind Method.GET to static(ResourceLoader.Classpath("public/new"))
        )

        val port = 8080
        println("Server started on port: $port")
        Jetty(port).toServer(app).start()
    }
}

fun main(args: Array<String>) {
    Server.start()
}