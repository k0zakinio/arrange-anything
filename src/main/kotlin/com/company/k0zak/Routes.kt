package com.company.k0zak

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import java.sql.Connection
import java.sql.DriverManager

object Routes {

    object GET {
        val event: HttpHandler = {
            Response(Status.OK).body("Nice one :)")
        }
    }

    object POST {
        val event: HttpHandler = {
            Class.forName("org.postgresql.Driver")
            val url = "jdbc:postgresql://postgres.local:5432/testdb"
            val con: Connection = DriverManager.getConnection(url, "postgres", "testpassword")

            val statement = con.prepareStatement("INSERT INTO EVENTS (title, owner_name) VALUES (?, ?)")
            statement.setString(1, "My First Event")
            statement.setString(2, "Andrew")
            statement.execute()

            println("should be calling query...")

            Response(Status.CREATED).body("Something was meant to be created :)")
        }
    }
}