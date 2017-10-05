package com.company.k0zak

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status

object Routes {
    object GET {
        val event: HttpHandler = {
            Response(Status.OK).body("Nice one :)")
        }
    }

    object POST {
        val event: HttpHandler = {
            Response(Status.CREATED).body("Something was meant to be created :)")
        }
    }
}