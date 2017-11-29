package com.company.k0zak

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventDateParserTest {
    private val parser = EventDateParser(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @Test
    fun `can parse from ISO_LOCAL_DATE_TIME format`() {
        val input = "2017-06-15T12:00"

        val localDateTime = parser.parse(input)

        assertThat(localDateTime, equalTo(LocalDateTime.of(2017, 6, 15, 12, 0)))
    }
}