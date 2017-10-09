package com.company.k0zak

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import java.sql.Connection
import java.sql.DriverManager

object Routes {

    object GET {
        val event: HttpHandler = {
            println("something something something")
            Response(Status.OK).body("Nice one :)")
        }
    }

    object POST {
        val event: HttpHandler = {
            println("trying some shit")
            Class.forName("org.postgresql.Driver")
            val url = "jdbc:postgresql://172.18.0.2:5432/testdb"
            val con: Connection = DriverManager.getConnection(url, "postgres", "testpassword")

            val query = "CREATE TABLE foo (id SERIAL PRIMARY KEY, title TEXT);"
            val statement = con.createStatement()
            statement.execute(query)

            println("should be calling query: $query")

            Response(Status.CREATED).body("Something was meant to be created :)")
        }
    }
}