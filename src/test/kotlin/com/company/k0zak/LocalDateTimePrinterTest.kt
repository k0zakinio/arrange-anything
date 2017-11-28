package com.company.k0zak

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimePrinterTest {
    private val printer = LocalDateTimePrinter(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    @Test
    fun `can print into ISO-8601 standard`() {
        val ldt = LocalDateTime.of(2017, 6, 13, 12, 0)

        val printed = printer.print(ldt)

        assertThat(printed, equalTo("2017-06-13T12:00:00"))
    }
}