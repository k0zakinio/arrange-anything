package com.company.k0zak.dao

import com.company.k0zak.db.JDBCClient
import com.company.k0zak.model.Event
import java.sql.ResultSet

class EventsDao(private val dbClient: JDBCClient) {
    fun insertEvent(event: Event) {
        val statement = dbClient.preparedStatement("INSERT INTO EVENTS (owner_name, title) VALUES (?, ?)")
        statement.setString(1, event.owner)
        statement.setString(2, event.title)
        statement.execute()
        statement.close()
    }

    fun getAllEvents(): List<Event> {
        val statement = dbClient.preparedStatement("SELECT * FROM EVENTS")
        return statement.executeQuery().use { resultSetToListOfEvents(it) }
    }

    fun getEventForId(id: String): Event? {
        val statement = dbClient.preparedStatement("SELECT * FROM EVENTS WHERE id = ?")
        statement.setInt(1, id.toInt())
        val executeQuery = statement.executeQuery()

        return if(executeQuery.next()) return Event(executeQuery.getString(3), executeQuery.getString(2)) else null
    }

    fun getEventsForUser(ownerName: String): List<Event> {
        val statement = dbClient.preparedStatement("SELECT * FROM EVENTS WHERE owner_name = ?")
        statement.setString(1, ownerName)
        return statement.executeQuery().use { resultSetToListOfEvents(it) }
    }

    private fun resultSetToListOfEvents(rs: ResultSet): List<Event> {
        val result = mutableListOf<Event>()
        while (rs.next()) {
            result.add(Event(rs.getString(3), rs.getString(2)))
        }
        return result
    }
}