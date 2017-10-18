package com.company.k0zak

import org.http4k.core.*
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
object App {
    fun start() {
        val app: RoutingHttpHandler = routes(
                "/event" bind Method.GET to Routes.GET.event,
                "/event" bind Method.POST to Routes.POST.event
        )

        val port = 8080
        println("Server started on port: $port")
        Jetty(port).toServer(app).start()
    }
}

fun main(args: Array<String>) {
    App.start()
}