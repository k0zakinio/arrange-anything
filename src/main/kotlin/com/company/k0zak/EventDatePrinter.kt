package com.company.k0zak

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.temporal.ChronoField
import java.time.format.DateTimeFormatterBuilder


class EventDatePrinter(private val dateTimeFormatter: DateTimeFormatter) {
    fun print(ldt: LocalDateTime): String {
        return ldt.format(dateTimeFormatter)
    }

    companion object {
        private val DAYS_LOOKUP = IntRange(1, 31).map { it.toLong() to getOrdinal(it) }.toMap()

        val viewFormatter = DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH, DAYS_LOOKUP)
                .appendLiteral(" ")
                .appendPattern("MMMM")
                .appendLiteral(" ")
                .appendPattern("yyyy")
                .appendLiteral(", ")
                .appendPattern("hh")
                .appendLiteral(":")
                .appendPattern("mm")
                .appendLiteral(":")
                .appendPattern("ss")
                .appendLiteral(" ")
                .parseCaseInsensitive()
                .appendPattern("a")
                .toFormatter(Locale.ENGLISH)!!

        private fun getOrdinal(n: Int): String {
            if (n in 11..13) {
                return n.toString() + "th"
            }
            return when (n % 10) {
                1 -> n.toString() + "st"
                2 -> n.toString() + "nd"
                3 -> n.toString() + "rd"
                else -> n.toString() + "th"
            }
        }
    }
}