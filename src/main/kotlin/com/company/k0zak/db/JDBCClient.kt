package com.company.k0zak.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class JDBCClient(jdbcConfig: JDBCConfig) {

    init {
        Class.forName(jdbcConfig.driver)
    }

    private val con: Connection = DriverManager.getConnection(
            jdbcConfig.toUri(),
            jdbcConfig.username,
            jdbcConfig.password
    )

    fun preparedStatement(sql: String): PreparedStatement {
        return con.prepareStatement(sql)
    }
}