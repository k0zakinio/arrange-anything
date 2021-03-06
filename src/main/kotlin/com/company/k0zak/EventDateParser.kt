package com.company.k0zak

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventDateParser(private val format: DateTimeFormatter) {
    fun parse(input: String): LocalDateTime {
        return LocalDateTime.parse(input, format)
    }
}