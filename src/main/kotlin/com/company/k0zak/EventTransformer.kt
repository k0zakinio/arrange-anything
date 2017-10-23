package com.company.k0zak

import com.company.k0zak.model.Event
import java.sql.Connection
import java.sql.DriverManager

object EventTransformer {

    private val driver = Class.forName("org.postgresql.Driver")
    private val url = "jdbc:postgresql://postgres.local:5432/testdb"
    private val con: Connection = DriverManager.getConnection(url, "postgres", "testpassword")

    fun insertEvent(event: Event) {
        val statement = con.prepareStatement("INSERT INTO EVENTS (owner_name, title) VALUES (?, ?)")
        statement.setString(1, event.owner)
        statement.setString(2, event.title)
        statement.execute()
        statement.close()
    }

    fun getAllEvents(): List<Event> {
        val statement = con.prepareStatement("SELECT * FROM EVENTS")
        val executeQuery = statement.executeQuery()

        val result = mutableListOf<Event>()

        while (executeQuery.next()) {
            val owner = executeQuery.getString(2)
            val title = executeQuery.getString(3)
            result.add(Event(title, owner))
        }
        return result
    }
}