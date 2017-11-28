package com.company.k0zak

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimePrinter(private val dateTimeFormatter: DateTimeFormatter) {
    fun print(ldt: LocalDateTime): String {
        return ldt.format(dateTimeFormatter)
    }
}