package com.company.k0zak.db_helpers

import com.company.k0zak.db.JDBCConfig
import org.flywaydb.core.Flyway

object DBHelper {

    private val flyway = Flyway()
    val testConfig: JDBCConfig = JDBCConfig(
            username = "postgres",
            password = "testpassword",
            hostname = "postgres.local",
            port = 5432,
            dbName = "testdb",
            driver = "org.postgresql.Driver"
    )

    init {
        flyway.setDataSource(testConfig.toUri(), testConfig.username, testConfig.password)
    }

    fun cleanDatabase() {
        flyway.clean()
        flyway.migrate()
    }
}