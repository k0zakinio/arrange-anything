package com.company.k0zak.db_helpers

import com.company.k0zak.db.JDBCClient
import com.company.k0zak.db.JDBCConfig
import org.flywaydb.core.Flyway

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
}