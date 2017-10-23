package com.company.k0zak.db_helpers

import org.flywaydb.core.Flyway

object DBHelper {

    private val flyway = Flyway()

    init {
        flyway.setDataSource("jdbc:postgresql://postgres.local:5432/testdb", "postgres", "testpassword")
    }

    fun cleanDatabase() {
        flyway.clean()
        flyway.migrate()
    }
}