package com.company.k0zak.dao

import com.company.k0zak.db.JDBCClient
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User

class UserDao(private val dbClient: JDBCClient) {

    fun newUser(user: User) {
        val preparedStatement = dbClient.preparedStatement("INSERT INTO USERS (username, password_hash) VALUES (?, ?)")
        preparedStatement.setString(1, user.username)
        preparedStatement.setString(2, user.password)
        preparedStatement.execute()
        preparedStatement.close()
    }

    fun getUser(username: String): User? {
        val preparedStatement = dbClient.preparedStatement("SELECT username, password_hash FROM USERS WHERE username = ?")
        preparedStatement.setString(1, username)
        val rs = preparedStatement.executeQuery()

        return if (rs.next()) {
            return User(rs.getString(1), rs.getString(2))
        } else null
    }

    fun getUserFromCookie(cookie: String): AuthenticatedUser? {
        val preparedStatement = dbClient.preparedStatement("SELECT u.username FROM SESSION s INNER JOIN USERS u ON s.user_id = u.id WHERE s.session_id = ?")
        preparedStatement.setString(1, cookie)
        val rs = preparedStatement.executeQuery()

        return if (rs.next()) AuthenticatedUser(rs.getString(1)) else null
    }

    fun newSession(user: User, sessionId: String) {
        val preparedStatement = dbClient.preparedStatement(
                "INSERT INTO SESSION (user_id, session_id) VALUES ( (SELECT id FROM USERS WHERE username = ?), ?)")
        preparedStatement.setString(1, user.username)
        preparedStatement.setString(2, sessionId)
        preparedStatement.execute()
        preparedStatement.close()
    }

}