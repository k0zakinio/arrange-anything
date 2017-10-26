package com.company.k0zak.db

data class JDBCConfig(val username: String, val password: String, val hostname: String, val port: Int, val dbName: String, val driver: String) {
    fun toUri(): String = "jdbc:postgresql://$hostname:$port/$dbName"
}