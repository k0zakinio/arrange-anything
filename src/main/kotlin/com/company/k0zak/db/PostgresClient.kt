package com.company.k0zak.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class PostgresClient(pgConfig: PgConfig) {

    init {
        Class.forName("org.postgresql.Driver")
    }

    private val con: Connection = DriverManager.getConnection(
            pgConfig.toUri(),
            pgConfig.username,
            pgConfig.password
    )

    fun preparedStatement(sql: String): PreparedStatement {
        return con.prepareStatement(sql)
    }
}