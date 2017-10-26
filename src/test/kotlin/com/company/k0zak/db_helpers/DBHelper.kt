package com.company.k0zak.db_helpers

import com.company.k0zak.db.JDBCConfig
import org.flywaydb.core.Flyway

object DBHelper {

    private val flyway = Flyway()
    val testConfig: JDBCConfig = JDBCConfig(
            username = "sa",
            password = "",
            driver = "org.h2.Driver",
            uri = null
    )

    init {
        flyway.setDataSource(testConfig.toUri(), testConfig.username, testConfig.password)
    }

    fun cleanDatabase() {
        flyway.clean()
        flyway.migrate()
    }
}