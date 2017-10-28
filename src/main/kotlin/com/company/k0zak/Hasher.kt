package com.company.k0zak

interface Hasher {
    fun hashString(str: String): String

    fun check(stored: String, current: String): Boolean
}