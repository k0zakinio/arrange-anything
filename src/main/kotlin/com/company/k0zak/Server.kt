package com.company.k0zak

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty

fun main(args: Array<String>) {
    val app: RoutingHttpHandler = routes("/new" bind Method.GET to {_ : Request -> Response(Status.OK).body("Yo, how's it going!?")})

    val port = 80
    println("Server started on port: $port")
    Jetty(port).toServer(app).start()


}