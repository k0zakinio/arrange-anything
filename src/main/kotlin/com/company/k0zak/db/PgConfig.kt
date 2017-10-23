package com.company.k0zak.db

data class PgConfig(val username: String, val password: String, val hostname: String, val port: Int, val dbName: String) {
    fun toUri(): String = "jdbc:postgresql://$hostname:$port/$dbName"
}