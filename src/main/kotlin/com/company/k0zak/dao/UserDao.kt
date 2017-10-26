package com.company.k0zak.dao

import com.company.k0zak.UserAuthenticator
import com.company.k0zak.db.JDBCClient
import com.company.k0zak.model.AuthenticatedUser
import com.company.k0zak.model.User

class UserDao(private val dbClient: JDBCClient, private val userAuthenticator: UserAuthenticator) {

    fun newUser(user: User) {
        val preparedStatement = dbClient.preparedStatement("INSERT INTO USERS (username, password_hash) VALUES (?, ?)")
        preparedStatement.setString(1, user.username)
        preparedStatement.setString(2, userAuthenticator.hashPassword(user.password))
        preparedStatement.execute()
        preparedStatement.close()
    }

    fun getUser(loginUser: User): AuthenticatedUser? {
        val preparedStatement = dbClient.preparedStatement("SELECT username, password_hash FROM USERS WHERE username = ?")
        preparedStatement.setString(1, loginUser.username)
        val rs = preparedStatement.executeQuery()

        return if(rs.next()) {
            val storedUser = User(rs.getString(1), rs.getString(2))
            userAuthenticator.authenticate(storedUser, loginUser)
        } else null
    }

}