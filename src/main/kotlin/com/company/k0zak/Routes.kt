package com.company.k0zak

import org.http4k.core.*
import org.http4k.lens.Query
import org.http4k.lens.string
import java.sql.Connection
import java.sql.DriverManager

object Routes {

    object GET {

        private val ownerQuery = Query.string().required("owner")
        val event: HttpHandler = { req: Request ->
            val owner = ownerQuery(req)

            Class.forName("org.postgresql.Driver")
            val url = "jdbc:postgresql://postgres.local:5432/testdb"
            val con: Connection = DriverManager.getConnection(url, "postgres", "testpassword")
            val statement = con.prepareStatement("SELECT json_agg(json_build_object('title', title, 'owner', owner_name)) FROM events WHERE owner_name = ?")
            statement.setString(1, owner)
            val resultSet = statement.executeQuery()
            resultSet.next()
            val json = resultSet.getString(1)

            Response(Status.OK).header("Content-Type", "application/json").body(json)
        }
    }

    object POST {

        val event: HttpHandler = { req: Request ->
            Class.forName("org.postgresql.Driver")
            val url = "jdbc:postgresql://postgres.local:5432/testdb"
            val con: Connection = DriverManager.getConnection(url, "postgres", "testpassword")

            val receivedForm = MultipartFormBody.from(req)
            val title = receivedForm.field("title")!!
            val owner = receivedForm.field("owner")!!

            val statement = con.prepareStatement("INSERT INTO EVENTS (title, owner_name) VALUES (?, ?)")
            statement.setString(1, title)
            statement.setString(2, owner)
            statement.execute()

            println("should be calling query...")

            Response(Status.CREATED).body("Something was meant to be created :)")
        }
    }
}