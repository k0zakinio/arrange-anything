package com.company.k0zak

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import java.sql.Connection
import java.sql.DriverManager

object Routes {
    private val url = "jdbc:testuser:testpassword//127.0.0.1:5432/testdb"
    val con: Connection = DriverManager.getConnection(url)

    object GET {
        val event: HttpHandler = {
            Response(Status.OK).body("Nice one :)")
        }
    }

    object POST {
        val event: HttpHandler = {
            val query = "INSERT TABLE foo (id SERIAL PRIMARY KEY, title TEXT);"
            val prepareCall = con.prepareCall(query)

            println("should be calling query: $query")

            prepareCall.execute()

            Response(Status.CREATED).body("Something was meant to be created :)")
        }
    }
}