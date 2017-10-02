package com.company.k0zak

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status

object Routes {
    val routesNew: HttpHandler = { Response(Status.OK).body("Nice one :)") }
}