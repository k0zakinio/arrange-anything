package com.company.k0zak.db_helpers

import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db.JDBCConfig
import org.flywaydb.core.Flyway
import java.sql.ResultSet
import java.sql.SQLException

object TestDBHelper {

    private val flyway = Flyway()
    private val testConfig: JDBCConfig = JDBCConfig(
            username = "sa",
            password = "",
            driver = "org.h2.Driver",
            uri = null
    )

    val testDbClient: JDBCClient = JDBCClient(testConfig)

    init {
        flyway.setDataSource(testConfig.toUri(), testConfig.username, testConfig.password)
    }

    fun cleanDatabase() {
        flyway.clean()
        flyway.migrate()
    }

    fun <T>executeQuery(sql: String, fn: (ResultSet) -> T): T {
        val executeQuery = testDbClient.preparedStatement(sql).executeQuery()
        try {
            executeQuery.next()
        } catch (e: SQLException) {
            println("************************************")
            println("No results returned from query: $sql")
            println("************************************")
            e.printStackTrace()
        }
        return fn(executeQuery)
    }
}