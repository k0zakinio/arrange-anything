package com.company.k0zak.dao

import com.company.k0zak.EventDateParser
import com.company.k0zak.EventDatePrinter
import com.company.k0zak.db.JDBCClient
import com.company.k0zak.model.Event
import java.sql.ResultSet

class EventsDao(private val dbClient: JDBCClient, private val dateParser: EventDateParser, private val datePrinter: EventDatePrinter) {
    fun insertEvent(event: Event) {
        val statement = dbClient.preparedStatement("INSERT INTO EVENTS (owner_name, title, event_date) VALUES (?, ?, ?)")
        statement.setString(1, event.owner)
        statement.setString(2, event.title)
        statement.setString(3, datePrinter.print(event.date))
        statement.execute()
        statement.close()
    }

    fun getAllEvents(): List<Event> {
        val statement = dbClient.preparedStatement("SELECT owner_name, title, event_date FROM EVENTS")
        return statement.executeQuery().use { resultSetToListOfEvents(it) }
    }

    fun getEventForId(id: String): Event? {
        val statement = dbClient.preparedStatement("SELECT owner_name, title, event_date FROM EVENTS WHERE id = ?")
        statement.setInt(1, id.toInt())
        val rs = statement.executeQuery()

        return if(rs.next()) {
            val dateString = rs.getString(3)
            return Event(rs.getString(1), rs.getString(2), dateParser.parse(dateString))
        } else null
    }

    fun getEventsForUser(ownerName: String): List<Event> {
        val statement = dbClient.preparedStatement("SELECT owner_name, title, event_date FROM EVENTS WHERE owner_name = ?")
        statement.setString(1, ownerName)
        return statement.executeQuery().use { resultSetToListOfEvents(it) }
    }

    private fun resultSetToListOfEvents(rs: ResultSet): List<Event> {
        val result = mutableListOf<Event>()
        while (rs.next()) {
            val dateString = rs.getString(3)
            result.add(Event(rs.getString(1), rs.getString(2), dateParser.parse(dateString)))
        }
        return result
    }
}