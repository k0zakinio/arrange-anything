package com.company.k0zak.db

data class JDBCConfig(val username: String, val password: String, val uri: String?, val driver: String) {
    fun toUri(): String {
        return if (uri.isNullOrBlank()) {
            "jdbc:h2:~/tmp"
        } else {
            "jdbc:postgresql://$uri"
        }
    }
}