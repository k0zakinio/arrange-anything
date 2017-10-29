package com.company.k0zak

import com.company.k0zak.dao.EventsDao
import com.company.k0zak.dao.UserDao
import com.company.k0zak.routes.EventsRoute
import com.company.k0zak.routes.UserRoute
import com.company.k0zak.routes.ViewEventsRoute
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.*
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty

class Server(private val eventsDao: EventsDao, private val userDao: UserDao, private val userAuthenticator: UserAuthenticator) {
    private lateinit var server: Http4kServer

    fun start() {
        val newEvents = EventsRoute(eventsDao, userAuthenticator)
        val userRoute = UserRoute(userDao, userAuthenticator)
        val viewEvents = ViewEventsRoute(eventsDao, userAuthenticator)

        val app: RoutingHttpHandler = routes(
                "/postNew" bind GET to newEvents.getNew,
                "/postNew" bind POST to newEvents.postNew,
                "/view" bind GET to viewEvents.all,
                "/view/{id}" bind GET to viewEvents.byId,
                "/create-account" bind GET to static(ResourceLoader.Classpath("public/create-account")),
                "/create-account" bind POST to userRoute.newUser,
                "/created" bind GET to static(ResourceLoader.Classpath("public/created")),
                "/login" bind GET to static(ResourceLoader.Classpath("public/login")),
                "/login" bind POST to userRoute.loginUser
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
    Server(Dependencies.eventsDao, Dependencies.userDao, Dependencies.userAuthenticator).start()
}