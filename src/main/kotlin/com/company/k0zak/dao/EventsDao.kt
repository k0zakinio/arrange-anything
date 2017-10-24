package com.company.k0zak.dao

import com.company.k0zak.db.PostgresClient
import com.company.k0zak.model.Event

class EventsDao(private val pgClient: PostgresClient) {
    fun insertEvent(event: Event) {
        val statement = pgClient.preparedStatement("INSERT INTO EVENTS (owner_name, title) VALUES (?, ?)")
        statement.setString(1, event.owner)
        statement.setString(2, event.title)
        statement.execute()
        statement.close()
    }

    fun getAllEvents(): List<Event> {
        val statement = pgClient.preparedStatement("SELECT * FROM EVENTS")
        val executeQuery = statement.executeQuery()
        val result = mutableListOf<Event>()

        while (executeQuery.next()) {
            val owner = executeQuery.getString(2)
            val title = executeQuery.getString(3)
            result.add(Event(title, owner))
        }
        return result
    }

    fun getEventForId(id: String): Event? {
        val statement = pgClient.preparedStatement("SELECT * FROM EVENTS WHERE id = ?")
        statement.setInt(1, id.toInt())
        val executeQuery = statement.executeQuery()

        return if(executeQuery.next()) return Event(executeQuery.getString(2), executeQuery.getString(3)) else null
    }
}