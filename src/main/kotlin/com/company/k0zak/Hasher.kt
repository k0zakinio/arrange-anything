package com.company.k0zak

interface Hasher {
    fun hashString(text: String): String

    fun check(current: String, stored: String): Boolean
}